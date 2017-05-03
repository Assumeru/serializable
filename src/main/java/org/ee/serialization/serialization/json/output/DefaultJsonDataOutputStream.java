package org.ee.serialization.serialization.json.output;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.ee.serialization.SerializationException;
import org.ee.serialization.serialization.CachingSerializer;
import org.ee.serialization.serialization.ClassDescriptor;
import org.ee.serialization.serialization.json.JsonSerializable;
import org.ee.serialization.serialization.json.output.writer.JsonWriter;

public class DefaultJsonDataOutputStream extends CachingSerializer implements JsonDataOutputStream {
	private final JsonWriter output;
	private final boolean addVersion;
	private final Map<Class<?>, ClassDescriptor> descriptors;

	public DefaultJsonDataOutputStream(JsonWriter output, boolean addVersion) throws IOException {
		this.output = output;
		this.addVersion = addVersion;
		descriptors = new HashMap<>();
	}

	@Override
	protected void writeObjectOrReference(Object object) throws IOException {
		if(object == null) {
			value((String) null);
		} else if(object.getClass() == byte[].class) {
			value(Base64.encodeBase64String((byte[]) object));
		} else if(object.getClass().isArray()) {
			beginObject();
			name(PROPERTY_CLASS).value(object.getClass().getName());
			name(PROPERTY_ARRAY_VALUES).beginArray();
			for(int i = 0; i < Array.getLength(object); i++) {
				writeObject(Array.get(object, i));
			}
			endArray();
			endObject();
		} else if(object.getClass() == String.class) {
			value((String) object);
		} else if(object.getClass() == Boolean.class) {
			value((Boolean) object);
		} else if(object instanceof Number) {
			value((Number) object);
		} else if(object instanceof JsonSerializable) {
			((JsonSerializable) object).toJson(this);
		} else if(object instanceof Enum) {
			Enum<?> e = (Enum<?>) object;
			beginObject();
			name(PROPERTY_CLASS).value(object.getClass().getName());
			name(PROPERTY_ENUM_VALUE).value(e.name());
			endObject();
		} else {
			try {
				ClassDescriptor descriptor = getClassDescriptor(object.getClass());
				beginObject();
				name(PROPERTY_CLASS).value(descriptor.getName());
				if(addVersion && descriptor.getSerialVersion() != null) {
					name(PROPERTY_VERSION).value(descriptor.getSerialVersion());
				}
				name(PROPERTY_FIELDS).beginObject();
				for(Field field : descriptor.getFields()) {
					Object value = field.get(object);
					if(value != null) {
						Class<?> declaringClass = field.getDeclaringClass();
						if(declaringClass != object.getClass()) {
							name(field.getDeclaringClass().getName() + "." + field.getName());
						} else {
							name(field.getName());
						}
						writeObject(value);
					}
				}
				endObject();
				endObject();
			} catch(IllegalAccessException e) {
				throw new SerializationException("Failed to serialize object with " + object.getClass(), e);
			}
		}
	}

	private ClassDescriptor getClassDescriptor(Class<?> type) throws SerializationException {
		ClassDescriptor descriptor = descriptors.get(type);
		if(descriptor == null) {
			descriptor = ClassDescriptor.of(type);
			descriptors.put(type, descriptor);
		}
		return descriptor;
	}

	@Override
	public JsonDataOutputStream beginArray() throws IOException {
		output.beginArray();
		return this;
	}

	@Override
	public JsonDataOutputStream beginObject() throws IOException {
		output.beginObject();
		return this;
	}

	@Override
	public void close() throws IOException {
		output.close();
	}

	@Override
	public JsonDataOutputStream endArray() throws IOException {
		output.endArray();
		return this;
	}

	@Override
	public JsonDataOutputStream endObject() throws IOException {
		output.endObject();
		return this;
	}

	@Override
	public JsonDataOutputStream name(String name) throws IOException {
		output.name(name);
		return this;
	}

	@Override
	public JsonDataOutputStream nullValue() throws IOException {
		output.nullValue();
		return this;
	}

	@Override
	public JsonDataOutputStream value(boolean value) throws IOException {
		output.value(value);
		return this;
	}

	@Override
	public JsonDataOutputStream value(double value) throws IOException {
		output.value(value);
		return this;
	}

	@Override
	public JsonDataOutputStream value(Boolean value) throws IOException {
		output.value(value);
		return this;
	}

	@Override
	public JsonDataOutputStream value(Number value) throws IOException {
		output.value(value);
		return this;
	}

	@Override
	public JsonDataOutputStream value(String value) throws IOException {
		output.value(value);
		return this;
	}

	@Override
	public JsonDataOutputStream value(long value) throws IOException {
		output.value(value);
		return this;
	}
}
