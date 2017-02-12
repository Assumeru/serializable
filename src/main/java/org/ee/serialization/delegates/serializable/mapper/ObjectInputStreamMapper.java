package org.ee.serialization.delegates.serializable.mapper;

import java.io.IOException;

public interface ObjectInputStreamMapper {
	Object map(Object object, ObjectInputStreamMapper mapper) throws IOException, ClassNotFoundException;
}
