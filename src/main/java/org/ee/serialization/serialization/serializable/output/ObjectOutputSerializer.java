package org.ee.serialization.serialization.serializable.output;

import java.io.IOException;

import org.ee.serialization.Config;
import org.ee.serialization.Serializer;
import org.ee.serialization.deserialization.serializable.mapper.model.CachingObjectOutput;

public interface ObjectOutputSerializer extends Serializer, CachingObjectOutput {
	Config getConfig();

	StreamBuffer getStreamBuffer();

	void writeObject(Object object, ObjectOutputSerializer serializer) throws IOException;
}
