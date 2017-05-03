package org.ee.serialization.deserialization.serializable.mapper.standard;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.ee.serialization.deserialization.serializable.mapper.ObjectInputStreamMapper;
import org.ee.serialization.deserialization.serializable.mapper.ObjectInputStreamMapperDelegate;
import org.ee.serialization.deserialization.serializable.mapper.model.ObjectMapping;

public class PrimitiveMapper implements ObjectInputStreamMapperDelegate {
	private static final Set<String> PRIMITIVE_NAMES = new HashSet<>(Arrays.asList(
			Boolean.class.getName(), Byte.class.getName(),
			Character.class.getName(), Double.class.getName(),
			Float.class.getName(), Integer.class.getName(),
			Long.class.getName(), Short.class.getName()));

	@Override
	public Object map(Object object, ObjectInputStreamMapper mapper) throws IOException, ClassNotFoundException {
		if(object instanceof ObjectMapping) {
			ObjectMapping mapping = (ObjectMapping) object;
			return mapping.getFields().get(0).getValue();
		}
		return object;
	}

	@Override
	public boolean canMap(Object object) {
		if(object instanceof ObjectMapping) {
			return PRIMITIVE_NAMES.contains(((ObjectMapping) object).getDescription().getName());
		}
		return false;
	}
}
