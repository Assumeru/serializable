package org.ee.serialization.serialization.serializable.mapper;

import java.io.IOException;

import org.ee.serialization.deserialization.serializable.mapper.model.ObjectOutputWriteable;
import org.ee.serialization.serialization.serializable.output.ObjectOutputSerializer;

public class ObjectMapper implements SerializableMapper {
	private final ClassDescriptionManager cache;

	public ObjectMapper(ClassDescriptionManager cache) {
		this.cache = cache;
	}

	@Override
	public boolean canMap(Object object) {
		return true;
	}

	@Override
	public void map(Object object, ObjectOutputSerializer output) throws IOException {
		if(object instanceof ObjectOutputWriteable) {
			((ObjectOutputWriteable) object).writeTo(output);
		} else {
			cache.writeFromDescription(object, output);
		}
	}
}
