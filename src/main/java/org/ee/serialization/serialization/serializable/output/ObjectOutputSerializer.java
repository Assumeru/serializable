package org.ee.serialization.serialization.serializable.output;

import java.io.IOException;
import java.io.ObjectOutput;

import org.ee.serialization.Config;
import org.ee.serialization.Serializer;

public interface ObjectOutputSerializer extends Serializer, ObjectOutput {
	Config getConfig();

	StreamBuffer getStreamBuffer();

	void writeObject(Object object, ObjectOutputSerializer serializer) throws IOException;
}
