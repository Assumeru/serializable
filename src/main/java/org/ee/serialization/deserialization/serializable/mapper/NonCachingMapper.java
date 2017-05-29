package org.ee.serialization.deserialization.serializable.mapper;

public abstract class NonCachingMapper implements ObjectInputStreamMapperDelegate {
	@Override
	public void cache(Object object, Object mapping) {
	}

	@Override
	public Object getFromCache(Object object) {
		return null;
	}
}
