package org.ee.serialization.serialization.serializable.mapper;

import java.io.IOException;
import java.io.ObjectStreamConstants;

import org.ee.serialization.serialization.serializable.output.ObjectOutputSerializer;

public class ClassMapper implements SerializableMapper {
	private final ClassDescriptionManager cache;

	public ClassMapper(ClassDescriptionManager cache) {
		this.cache = cache;
	}

	@Override
	public boolean canMap(Object object) {
		return object instanceof Class;
	}

	@Override
	public void map(Object object, ObjectOutputSerializer output) throws IOException {
		Class<?> type = (Class<?>) object;
		output.writeByte(ObjectStreamConstants.TC_CLASS);
		output.writeObject(cache.getClassDescription(type, false));
		output.assignHandle(type);
	}
}
