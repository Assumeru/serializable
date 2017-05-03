package org.ee.serialization.deserialization.serializable;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ee.serialization.SerializationException;
import org.ee.serialization.deserialization.DeserializationDelegate;
import org.ee.serialization.deserialization.serializable.mapper.MapperDelegateInputStream;
import org.ee.serialization.deserialization.serializable.mapper.ObjectInputStreamMapper;
import org.ee.serialization.deserialization.serializable.mapper.model.ArrayMapping;
import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescription;
import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescriptionInfo;
import org.ee.serialization.deserialization.serializable.mapper.model.ClassMapping;
import org.ee.serialization.deserialization.serializable.mapper.model.EnumMapping;
import org.ee.serialization.deserialization.serializable.mapper.model.ExceptionMapping;
import org.ee.serialization.deserialization.serializable.mapper.model.Field;
import org.ee.serialization.deserialization.serializable.mapper.model.ObjectField;
import org.ee.serialization.deserialization.serializable.mapper.model.ObjectMapping;
import org.ee.serialization.deserialization.serializable.mapper.model.PrimitiveField;

public class ObjectInputStreamMapperDelegate implements DeserializationDelegate {
	private final MapperDelegateInputStream input;
	private final ObjectInputStreamMapper mapper;
	private final List<Object> references;

	public ObjectInputStreamMapperDelegate(InputStream input, ObjectInputStreamMapper mapper) throws IOException {
		this.input = new MapperDelegateInputStream(input);
		this.mapper = mapper;
		references = new ArrayList<>();
		readHeader();
	}

	private void readHeader() throws IOException {
		short magicNumber = input.readShort();
		short version = input.readShort();
		if(magicNumber != ObjectStreamConstants.STREAM_MAGIC || version != ObjectStreamConstants.STREAM_VERSION) {
			throw new SerializationException(String.format("Invalid header %04X%04X", magicNumber, version));
		}
	}

	@Override
	public void close() throws IOException {
		input.close();
	}

	private void readCheck(byte expected) throws IOException {
		byte value = input.readByte();
		if(value != expected) {
			throw new SerializationException(String.format("Unexpected byte: %02X expected %02X", value, expected));
		}
	}

	@Override
	public Object readObject() throws IOException, ClassNotFoundException {
		Object read = readObjectInternal();
		if(mapper == null) {
			return read;
		}
		return mapper.map(read, mapper);
	}

	/**
	 * https://docs.oracle.com/javase/7/docs/platform/serialization/spec/protocol.html
	 */
	private Object readObjectInternal() throws IOException {
		int next = input.peek();
		switch(next) {
		case ObjectStreamConstants.TC_ARRAY:
			return readNewArray();
		case ObjectStreamConstants.TC_CLASS:
			return readNewClass();
		case ObjectStreamConstants.TC_CLASSDESC:
			return readNewClassDescription();
		case ObjectStreamConstants.TC_ENUM:
			return readNewEnum();
		case ObjectStreamConstants.TC_EXCEPTION:
			return readException();
		case ObjectStreamConstants.TC_LONGSTRING:
			return readLongString();
		case ObjectStreamConstants.TC_NULL:
			readNullReference();
			return null;
		case ObjectStreamConstants.TC_OBJECT:
			return readNewObject();
		case ObjectStreamConstants.TC_REFERENCE:
			return readReference();
		case ObjectStreamConstants.TC_RESET:
			readReset();
			return null;
		case ObjectStreamConstants.TC_STRING:
			return readString();
		default:
			throw new SerializationException(String.format("Unexpected byte %02X", next));
		}
	}

	private ArrayMapping readNewArray() throws IOException {
		readCheck(ObjectStreamConstants.TC_ARRAY);
		ClassDescription description = readClassDescription();
		int size = input.readInt();
		Object[] values;
		ArrayMapping array;
		if(size > 0) {
			values = new Object[size];
			array = new ArrayMapping(description, values);
			references.add(array);
			char type = description.getName().charAt(1);
			for(int i = 0; i < size; i++) {
				values[i] = readType(type);
			}
		} else {
			array = new ArrayMapping(description);
			references.add(array);
		}
		return array;
	}

	private ClassMapping readNewClass() throws IOException {
		readCheck(ObjectStreamConstants.TC_CLASS);
		ClassMapping type = new ClassMapping(readClassDescription());
		references.add(type);
		return type;
	}

	private ClassDescription readClassDescription() throws IOException {
		int next = input.peek();
		switch(next) {
		case ObjectStreamConstants.TC_CLASSDESC:
			return readNewClassDescription();
		case ObjectStreamConstants.TC_NULL:
			readNullReference();
			return null;
		case ObjectStreamConstants.TC_REFERENCE:
			try {
				return (ClassDescription) readReference();
			} catch(ClassCastException e) {
				throw new SerializationException("Unexpected reference", e);
			}
		default:
			throw new SerializationException(String.format("Unexpected byte %02X", next));
		}
	}

	private ClassDescription readNewClassDescription() throws IOException {
		byte next = input.readByte();
		if(next == ObjectStreamConstants.TC_CLASSDESC) {
			ClassDescription description = new ClassDescription(input.readUTF(), input.readLong());
			references.add(description);
			description.setInfo(readClassDescriptionInfo(description));
			return description;
		} else if(next == ObjectStreamConstants.TC_PROXYCLASSDESC) {
			//TODO proxy classes
			throw new UnsupportedOperationException("No support for proxy classes");
		}
		throw new SerializationException(String.format("Unexpected byte %02X", next));
	}

	private ClassDescriptionInfo readClassDescriptionInfo(ClassDescription description) throws IOException {
		byte flags = input.readByte();
		Field[] fields = Field.createFields(input.readUnsignedShort());
		for(int i = 0; i < fields.length; i++) {
			char type = (char) input.readByte();
			String name = input.readUTF();
			if(ObjectField.isObject(type)) {
				fields[i] = new ObjectField(description, type, name, (String) readObjectInternal());
			} else if(PrimitiveField.isPrimitive(type)) {
				fields[i] = new PrimitiveField(description, type, name);
			} else {
				throw new SerializationException("Unexpected field type: " + type + " name: " + name);
			}
		}
		List<Object> annotation = readAnnotation();
		ClassDescription superClass = readClassDescription();
		return new ClassDescriptionInfo(flags, fields, annotation, superClass);
	}

	public List<Object> readAnnotation() throws IOException {
		int next = input.peek();
		List<Object> contents;
		if(next == ObjectStreamConstants.TC_ENDBLOCKDATA) {
			contents = Collections.emptyList();
		} else {
			contents = readContents();
		}
		readCheck(ObjectStreamConstants.TC_ENDBLOCKDATA);
		return contents;
	}

	private List<Object> readContents() throws IOException {
		List<Object> contents = new ArrayList<>();
		do {
			contents.add(readContent());
		} while(hasContent());
		return contents;
	}

	private boolean hasContent() throws IOException {
		int next = input.peek();
		switch(next) {
		case ObjectStreamConstants.TC_BLOCKDATA:
		case ObjectStreamConstants.TC_BLOCKDATALONG:
		case ObjectStreamConstants.TC_ARRAY:
		case ObjectStreamConstants.TC_CLASS:
		case ObjectStreamConstants.TC_CLASSDESC:
		case ObjectStreamConstants.TC_ENUM:
		case ObjectStreamConstants.TC_EXCEPTION:
		case ObjectStreamConstants.TC_LONGSTRING:
		case ObjectStreamConstants.TC_NULL:
		case ObjectStreamConstants.TC_OBJECT:
		case ObjectStreamConstants.TC_REFERENCE:
		case ObjectStreamConstants.TC_RESET:
		case ObjectStreamConstants.TC_STRING:
			return true;
		default:
			return false;
		}
	}

	private Object readContent() throws IOException {
		int next = input.peek();
		if(next == ObjectStreamConstants.TC_BLOCKDATA) {
			readCheck(ObjectStreamConstants.TC_BLOCKDATA);
			return input.read(input.read());
		} else if(next == ObjectStreamConstants.TC_BLOCKDATALONG) {
			readCheck(ObjectStreamConstants.TC_BLOCKDATALONG);
			return input.read(input.readInt());
		}
		return readObjectInternal();
	}

	private EnumMapping readNewEnum() throws IOException {
		readCheck(ObjectStreamConstants.TC_ENUM);
		try {
			return new EnumMapping(readClassDescription(), (String) readObjectInternal());
		} catch(ClassCastException e) {
			throw new SerializationException("Unexpected object", e);
		}
	}

	private ExceptionMapping readException() throws IOException {
		readCheck(ObjectStreamConstants.TC_EXCEPTION);
		readReset();
		try {
			ObjectMapping object = (ObjectMapping) readObjectInternal();
			readReset();
			return new ExceptionMapping(object);
		} catch(ClassCastException e) {
			throw new SerializationException("Throwable not an object", e);
		}
	}

	private String readLongString() throws IOException {
		readCheck(ObjectStreamConstants.TC_LONGSTRING);
		String utf = input.readLongUtf();
		references.add(utf);
		return utf;
	}

	private void readNullReference() throws IOException {
		readCheck(ObjectStreamConstants.TC_NULL);
	}

	private ObjectMapping readNewObject() throws IOException {
		readCheck(ObjectStreamConstants.TC_OBJECT);
		ObjectMapping object = new ObjectMapping(readClassDescription());
		references.add(object);
		object.readClassData(this);
		return object;
	}

	private Object readReference() throws IOException {
		readCheck(ObjectStreamConstants.TC_REFERENCE);
		int handle = input.readInt();
		int index = handle - ObjectStreamConstants.baseWireHandle;
		if(index < 0 || index >= references.size()) {
			throw new SerializationException("Unknown handle " + handle + " (" + index + ")");
		}
		return references.get(index);
	}

	private void readReset() throws IOException {
		readCheck(ObjectStreamConstants.TC_RESET);
		references.clear();
	}

	private String readString() throws IOException {
		readCheck(ObjectStreamConstants.TC_STRING);
		String utf = input.readUTF();
		references.add(utf);
		return utf;
	}

	public Object readType(char typeCode) throws IOException {
		switch(typeCode) {
		case 'B':
			return input.readByte();
		case 'C':
			return input.readChar();
		case 'D':
			return input.readDouble();
		case 'F':
			return input.readFloat();
		case 'I':
			return input.readInt();
		case 'J':
			return input.readLong();
		case 'S':
			return input.readShort();
		case 'Z':
			return input.readBoolean();
		case '[':
		case 'L':
			return readObjectInternal();
		default:
			throw new SerializationException("Unexpected field type: " + typeCode);
		}
	}
}
