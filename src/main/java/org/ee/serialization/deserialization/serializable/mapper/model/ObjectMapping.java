package org.ee.serialization.deserialization.serializable.mapper.model;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectStreamConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.ee.serialization.SerializationException;
import org.ee.serialization.deserialization.serializable.ObjectInputStreamMapperDelegate;
import org.ee.serialization.deserialization.serializable.mapper.ObjectInputStreamMapper;
import org.ee.serialization.serialization.json.JsonSerializable;
import org.ee.serialization.serialization.json.output.JsonDataOutputStream;

public class ObjectMapping implements JsonSerializable {
	private final ClassDescription description;
	private List<FieldValue> fields;
	private List<Object> data;

	public ObjectMapping(ClassDescription description) {
		this.description = description;
		fields = Collections.emptyList();
		data = Collections.emptyList();
	}

	public void readClassData(ObjectInputStreamMapperDelegate input) throws IOException {
		ClassDescriptionInfo info = description.getInfo();
		if(info.hasFlag(ObjectStreamConstants.SC_EXTERNALIZABLE)) {
			if(info.hasFlag(ObjectStreamConstants.SC_BLOCK_DATA)) {
				data = input.readAnnotation();
			} else {
				throw new UnsupportedOperationException("Only parseable by readExternal in " + description);
			}
		} else if(info.getSuperClass() == null) {
			fields = new ArrayList<>(info.getFields());
			data = new ArrayList<>();
			readClassData(description, input);
		} else {
			Deque<ClassDescription> stack = new LinkedList<>();
			ClassDescription current = description;
			int numFields = 0;
			while(current != null) {
				stack.push(current);
				info = current.getInfo();
				current = info.getSuperClass();
				numFields += info.getFields();
			}
			fields = new ArrayList<>(numFields);
			data = new ArrayList<>();
			while(!stack.isEmpty()) {
				current = stack.pop();
				readClassData(current, input);
			}
		}
	}

	private void readClassData(ClassDescription current, ObjectInputStreamMapperDelegate input) throws IOException {
		ClassDescriptionInfo info = current.getInfo();
		if(info.hasFlag(ObjectStreamConstants.SC_SERIALIZABLE)) {
			info.readFieldValues(input, fields);
			if(info.hasFlag(ObjectStreamConstants.SC_WRITE_METHOD)) {
				data.addAll(input.readAnnotation());
			}
		} else if(info.hasFlag(ObjectStreamConstants.SC_EXTERNALIZABLE)) {
			throw new SerializationException("Unexpected externalizable flag in " + current);
		} else {
			throw new SerializationException(String.format("Unknown flags %02X", info.getFlags()));
		}
	}

	@Override
	public String toString() {
		return description + " " + fields + " " + data;
	}

	public ClassDescription getDescription() {
		return description;
	}

	public List<FieldValue> getFields() {
		return fields;
	}

	public ObjectInput getData() {
		return new ListObjectInput(data, null);
	}

	public ObjectInput getData(ObjectInputStreamMapper mapper) {
		return new ListObjectInput(data, mapper);
	}

	@Override
	public void toJson(JsonDataOutputStream output) throws IOException {
		output.beginObject();
		output.name(JsonDataOutputStream.PROPERTY_CLASS).value(description.getName());
		output.name(JsonDataOutputStream.PROPERTY_VERSION).value(description.getSerialVersionUID());
		output.name(JsonDataOutputStream.PROPERTY_FIELDS).beginObject();
		for(FieldValue fieldValue : fields) {
			Object value = fieldValue.getValue();
			if(value != null) {
				Field field = fieldValue.getField();
				if(field.getDescription().equals(description)) {
					output.name(field.getName());
				} else {
					output.name(field.getDescription().getName() + "." + field.getName());
				}
				output.writeObject(value);
			}
		}
		output.endObject();
		output.name(JsonDataOutputStream.PROPERTY_DATA).beginArray();
		for(Object object : data) {
			output.writeObject(object);
		}
		output.endArray();
		output.endObject();
	}
}