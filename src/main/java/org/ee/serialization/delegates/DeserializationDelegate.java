package org.ee.serialization.delegates;

import java.io.Closeable;
import java.io.IOException;

public interface DeserializationDelegate extends Closeable {
	Object readObject() throws IOException, ClassNotFoundException;
}
