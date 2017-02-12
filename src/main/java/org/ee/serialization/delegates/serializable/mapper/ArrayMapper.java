package org.ee.serialization.delegates.serializable.mapper;

import java.io.IOException;
import java.lang.reflect.Array;

import org.ee.serialization.delegates.serializable.mapper.model.ArrayMapping;

public class ArrayMapper implements ObjectInputStreamMapperDelegate {
	@Override
	public Object map(Object object, ObjectInputStreamMapper mapper) throws IOException, ClassNotFoundException {
		if(object instanceof ArrayMapping) {
			ArrayMapping mapping = (ArrayMapping) object;
			Class<?> type = Class.forName(mapping.getDescription().getName());
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
		return object instanceof ArrayMapping && SerializableMapper.classExists(((ArrayMapping) object).getDescription());
	}
}
