package org.ee.serialization.serialization.serializable.output;

import java.io.ObjectOutput;

public interface CachingObjectOutput extends ObjectOutput {
	void assignHandle(Object object);
}
