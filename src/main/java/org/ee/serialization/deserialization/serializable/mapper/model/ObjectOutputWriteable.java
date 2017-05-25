package org.ee.serialization.deserialization.serializable.mapper.model;

import java.io.IOException;
import java.io.ObjectOutput;

public interface ObjectOutputWriteable {
	void writeTo(ObjectOutput output) throws IOException;
}
