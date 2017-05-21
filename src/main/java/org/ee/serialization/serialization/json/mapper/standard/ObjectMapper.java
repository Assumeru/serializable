package org.ee.serialization.serialization.json.mapper.standard;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.ee.serialization.SerializationException;
import org.ee.serialization.Config.Key;
import org.ee.serialization.serialization.ClassDescriptor;
import org.ee.serialization.serialization.json.mapper.JsonMapper;
import org.ee.serialization.serialization.json.output.JsonDataOutputStream;

public class ObjectMapper implements JsonMapper {
	public static final Key<Boolean> ADD_VERSION = new Key<>();
	private final Map<Class<?>, ClassDescriptor> descriptors;

	public ObjectMapper() {
		descriptors = new HashMap<>();
	}

	@Override
	public void map(Object object, JsonDataOutputStream output) throws IOException {
		if(object == null) {
			output.value((String) null);
			return;
		}
		Boolean addVersion = output.getConfig().get(ADD_VERSION);
		if(addVersion == null) {
			addVersion = false;
		}
		try {
			ClassDescriptor descriptor = getClassDescriptor(object.getClass());
			output.beginObject();
			output.name(JsonDataOutputStream.PROPERTY_CLASS).value(descriptor.getName());
			if(addVersion && descriptor.getSerialVersion() != null) {
				output.name(JsonDataOutputStream.PROPERTY_VERSION).value(descriptor.getSerialVersion());
			}
			output.name(JsonDataOutputStream.PROPERTY_FIELDS).beginObject();
			for(Field field : descriptor.getFields()) {
				Object value = field.get(object);
				if(value != null) {
					Class<?> declaringClass = field.getDeclaringClass();
					if(declaringClass != object.getClass()) {
						output.name(field.getDeclaringClass().getName() + "." + field.getName());
					} else {
						output.name(field.getName());
					}
					output.writeObject(value);
				}
			}
			output.endObject();
			output.endObject();
		} catch(IllegalAccessException e) {
			throw new SerializationException("Failed to serialize object with " + object.getClass(), e);
		}
	}

	@Override
	public boolean canMap(Object object) {
		return true;
	}

	private ClassDescriptor getClassDescriptor(Class<?> type) throws SerializationException {
		ClassDescriptor descriptor = descriptors.get(type);
		if(descriptor == null) {
			descriptor = ClassDescriptor.of(type);
			descriptors.put(type, descriptor);
		}
		return descriptor;
	}
}
