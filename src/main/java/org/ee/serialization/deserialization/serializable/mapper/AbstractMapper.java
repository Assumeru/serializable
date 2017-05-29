package org.ee.serialization.deserialization.serializable.mapper;

import java.io.IOException;

public abstract class AbstractMapper extends NonCachingMapper {
	@Override
	public Object map(Object object, ObjectInputStreamMapper mapper) throws IOException, ClassNotFoundException {
		Object mapping = mapper.getFromCache(object);
		if(mapping == null) {
			return doMap(object, mapper);
		}
		return mapping;
	}

	protected abstract Object doMap(Object object, ObjectInputStreamMapper mapper) throws IOException, ClassNotFoundException;
}
