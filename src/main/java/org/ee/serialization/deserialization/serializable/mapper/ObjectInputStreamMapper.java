package org.ee.serialization.deserialization.serializable.mapper;

import java.io.IOException;

public interface ObjectInputStreamMapper {
	Object map(Object object, ObjectInputStreamMapper mapper) throws IOException, ClassNotFoundException;
}
