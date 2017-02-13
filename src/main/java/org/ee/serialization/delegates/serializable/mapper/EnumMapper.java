package org.ee.serialization.delegates.serializable.mapper;

import java.io.IOException;
import java.util.Map;
import java.util.WeakHashMap;

import org.ee.serialization.SerializationException;
import org.ee.serialization.delegates.serializable.mapper.model.ClassDescription;
import org.ee.serialization.delegates.serializable.mapper.model.EnumMapping;
import org.ee.serialization.delegates.serializable.mapper.model.ObjectMapping;

public class EnumMapper implements ObjectInputStreamMapperDelegate {
	private final Map<ClassDescription, Boolean> mappable;

	public EnumMapper() {
		mappable = new WeakHashMap<>();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object map(Object object, ObjectInputStreamMapper mapper) throws IOException, ClassNotFoundException {
		if(object instanceof EnumMapping) {
			EnumMapping mapping = (EnumMapping) object;
			Class type = Class.forName(mapping.getDescription().getName());
			try {
				return Enum.valueOf(type, mapping.getName());
			} catch(IllegalArgumentException e) {
				throw new SerializationException("Failed to deserialize", e);
			}
		}
		return object;
	}

	@Override
	public boolean canMap(Object object) {
		if(object instanceof EnumMapping) {
			ClassDescription description = ((ObjectMapping) object).getDescription();
			Boolean canBeMapped = mappable.get(description);
			if(canBeMapped != null) {
				return canBeMapped;
			}
			canBeMapped = SerializableMapper.classExists(description);
			mappable.put(description, canBeMapped);
			return canBeMapped;
		}
		return false;
	}
}
