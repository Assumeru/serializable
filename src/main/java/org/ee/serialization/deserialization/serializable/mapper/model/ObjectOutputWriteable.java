package org.ee.serialization.deserialization.serializable.mapper.model;

import java.io.IOException;

import org.ee.serialization.serialization.serializable.output.CachingObjectOutput;

public interface ObjectOutputWriteable {
	void writeTo(CachingObjectOutput output) throws IOException;
}
