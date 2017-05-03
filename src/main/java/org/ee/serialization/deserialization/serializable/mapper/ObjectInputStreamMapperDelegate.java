package org.ee.serialization.deserialization.serializable.mapper;

public interface ObjectInputStreamMapperDelegate extends ObjectInputStreamMapper {
	boolean canMap(Object object);
}