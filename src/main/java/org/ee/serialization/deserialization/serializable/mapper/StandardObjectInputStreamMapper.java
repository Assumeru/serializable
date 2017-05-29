package org.ee.serialization.deserialization.serializable.mapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.ee.serialization.deserialization.serializable.mapper.model.ObjectMapping;
import org.ee.serialization.deserialization.serializable.mapper.standard.ArrayListMapper;
import org.ee.serialization.deserialization.serializable.mapper.standard.DateMapper;
import org.ee.serialization.deserialization.serializable.mapper.standard.HashMapMapper;
import org.ee.serialization.deserialization.serializable.mapper.standard.LinkedListMapper;
import org.ee.serialization.deserialization.serializable.mapper.standard.PrimitiveMapper;
import org.ee.serialization.deserialization.serializable.mapper.standard.SingleClassMapper;

public class StandardObjectInputStreamMapper extends NonCachingMapper {
	private static final Set<Class<?>> PRIMITIVES = new HashSet<>(Arrays.asList(Boolean.class, Byte.class, Character.class, Double.class, Float.class, Integer.class, Long.class, Short.class, String.class, Void.class));
	private static final ObjectInputStreamMapperDelegate PRIMITIVE_MAPPER = new PrimitiveMapper();
	private static final Map<String, SingleClassMapper<?>> MAPPERS = new HashMap<>();
	static {
		add(new ArrayListMapper());
		add(new DateMapper());
		add(new HashMapMapper());
		add(new LinkedListMapper());
	}

	private static void add(SingleClassMapper<?> mapper) {
		MAPPERS.put(mapper.getType().getName(), mapper);
	}

	@Override
	public Object map(Object object, ObjectInputStreamMapper mapper) throws ClassNotFoundException, IOException {
		if(object == null || PRIMITIVES.contains(object.getClass())) {
			return object;
		} else if(PRIMITIVE_MAPPER.canMap(object)) {
			return PRIMITIVE_MAPPER.map(object, mapper);
		} else if(object instanceof ObjectMapping) {
			String className = ((ObjectMapping) object).getDescription().getName();
			SingleClassMapper<?> delegate = MAPPERS.get(className);
			if(delegate != null) {
				return delegate.map(object, mapper);
			}
		}
		return object;
	}

	@Override
	public boolean canMap(Object object) {
		if(object == null || PRIMITIVES.contains(object.getClass()) || PRIMITIVE_MAPPER.canMap(object)) {
			return true;
		} else if(object instanceof ObjectMapping) {
			String className = ((ObjectMapping) object).getDescription().getName();
			return MAPPERS.containsKey(className);
		}
		return false;
	}
}
