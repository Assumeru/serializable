package org.ee.serialization;

import java.io.Closeable;
import java.io.IOException;

public interface Serializer extends Closeable {
	void writeObject(Object object) throws IOException;
}
