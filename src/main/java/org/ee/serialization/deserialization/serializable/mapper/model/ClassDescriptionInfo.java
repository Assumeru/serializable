package org.ee.serialization.deserialization.serializable.mapper.model;

import java.io.IOException;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.List;

import org.ee.serialization.deserialization.serializable.ObjectInputStreamMapperDelegate;

public class ClassDescriptionInfo implements ObjectOutputWriteable {
	private final byte flags;
	private final Field[] fields;
	private final List<Object> annotation;
	private final ClassDescription superClass;

	public ClassDescriptionInfo(byte flags, Field[] fields, List<Object> annotation, ClassDescription superClass) {
		this.flags = flags;
		this.fields = fields;
		this.annotation = annotation;
		this.superClass = superClass;
	}

	public boolean hasFlag(byte flag) {
		return (flags & flag) == flag;
	}

	public byte getFlags() {
		return flags;
	}

	public int getFields() {
		return fields.length;
	}

	public ClassDescription getSuperClass() {
		return superClass;
	}

	void readFieldValues(ObjectInputStreamMapperDelegate input, List<FieldValue> fieldValues) throws IOException {
		for(Field field : fields) {
			fieldValues.add(new FieldValue(field, input.readType(field.getTypeCode())));
		}
	}

	public void writeFieldValues(ObjectOutput output, Object object) throws NoSuchFieldException, SecurityException, ClassNotFoundException, IllegalAccessException, IOException {
		for(Field field : fields) {
			java.lang.reflect.Field f = field.getField();
			switch(field.getTypeCode()) {
			case 'B':
				output.writeByte(f.getByte(object));
				break;
			case 'C':
				output.writeChar(f.getChar(object));
				break;
			case 'D':
				output.writeDouble(f.getDouble(object));
				break;
			case 'F':
				output.writeFloat(f.getFloat(object));
				break;
			case 'I':
				output.writeInt(f.getInt(object));
				break;
			case 'J':
				output.writeLong(f.getLong(object));
				break;
			case 'S':
				output.writeShort(f.getShort(object));
				break;
			case 'Z':
				output.writeBoolean(f.getBoolean(object));
				break;
			case 'L':
			case '[':
				output.writeObject(f.get(object));
				break;
			}
		}
	}

	@Override
	public void writeTo(ObjectOutput output) throws IOException {
		output.writeByte(flags);
		output.writeShort(fields.length);
		for(Field field : fields) {
			field.writeTo(output);
		}
		ObjectMapping.writeBlockData(output, annotation);
		output.writeObject(superClass);
	}

	@Override
	public String toString() {
		return "INFO<" + flags + " " + Arrays.toString(fields) + " " + annotation + " " + superClass + ">";
	}
}
