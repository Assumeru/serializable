package org.ee.serialization;

import java.io.Closeable;
import java.io.IOException;

import org.ee.serialization.Config.Key;
import org.ee.serialization.serialization.ObjectFilter;

public interface Serializer extends Closeable {
	public static final Key<ObjectFilter> OBJECT_FILTER = new Key<>();

	void writeObject(Object object) throws IOException;
}
