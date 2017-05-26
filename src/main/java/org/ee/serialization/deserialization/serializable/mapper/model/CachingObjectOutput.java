package org.ee.serialization.deserialization.serializable.mapper.model;

import java.io.ObjectOutput;

public interface CachingObjectOutput extends ObjectOutput {
	void assignHandle(Object object);
}
