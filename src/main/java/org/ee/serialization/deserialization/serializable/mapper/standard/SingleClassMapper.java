package org.ee.serialization.deserialization.serializable.mapper.standard;

import java.io.IOException;

import org.ee.serialization.deserialization.serializable.mapper.ObjectInputStreamMapper;
import org.ee.serialization.deserialization.serializable.mapper.ObjectInputStreamMapperDelegate;

public interface SingleClassMapper<T> extends ObjectInputStreamMapperDelegate {
	T map(Object object, ObjectInputStreamMapper mapper) throws IOException, ClassNotFoundException;

	Class<T> getType();
}
