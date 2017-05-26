package org.ee.serialization.serialization.serializable.mapper;

import java.io.IOException;
import java.io.ObjectStreamConstants;

import org.ee.serialization.serialization.serializable.output.ObjectOutputSerializer;

public class EnumMapper implements SerializableMapper {
	private final ClassDescriptionManager cache;

	public EnumMapper(ClassDescriptionManager cache) {
		this.cache = cache;
	}

	@Override
	public boolean canMap(Object object) {
		return object instanceof Enum;
	}

	@Override
	public void map(Object object, ObjectOutputSerializer output) throws IOException {
		Enum<?> value = (Enum<?>) object;
		output.writeByte(ObjectStreamConstants.TC_ENUM);
		output.writeObject(cache.getClassDescription(value.getClass()));
		output.assignHandle(value);
		output.writeObject(value.name());
	}
}
