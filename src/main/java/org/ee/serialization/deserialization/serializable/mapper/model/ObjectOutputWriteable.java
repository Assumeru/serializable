package org.ee.serialization.deserialization.serializable.mapper.model;

import java.io.IOException;

public interface ObjectOutputWriteable {
	void writeTo(CachingObjectOutput output) throws IOException;
}
