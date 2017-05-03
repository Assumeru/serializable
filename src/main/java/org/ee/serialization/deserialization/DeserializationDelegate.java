package org.ee.serialization.deserialization;

import java.io.Closeable;
import java.io.IOException;

public interface DeserializationDelegate extends Closeable {
	Object readObject() throws IOException, ClassNotFoundException;
}
