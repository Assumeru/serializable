package org.ee.serialization.deserialization.serializable.mapper;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.WeakHashMap;

import org.ee.serialization.deserialization.serializable.mapper.model.ArrayMapping;
import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescription;
import org.ee.serialization.deserialization.serializable.mapper.model.ObjectMapping;

public class ArrayMapper implements ObjectInputStreamMapperDelegate {
	private final Map<ClassDescription, Boolean> mappable;

	public ArrayMapper() {
		mappable = new WeakHashMap<>();
	}

	@Override
	public Object map(Object object, ObjectInputStreamMapper mapper) throws IOException, ClassNotFoundException {
		if(object instanceof ArrayMapping) {
			ArrayMapping mapping = (ArrayMapping) object;
			Class<?> type = mapping.getDescription().getType();
			Object out = Array.newInstance(type.getComponentType(), mapping.size());
			for(int i = 0; i < mapping.size(); i++) {
				Array.set(out, i, mapper.map(mapping.get(i), mapper));
			}
			return out;
		}
		return object;
	}

	@Override
	public boolean canMap(Object object) {
		if(object instanceof ArrayMapping) {
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
