package org.ee.serialization.delegates.serializable.mapper;

public interface ObjectInputStreamMapperDelegate extends ObjectInputStreamMapper {
	boolean canMap(Object object);
}