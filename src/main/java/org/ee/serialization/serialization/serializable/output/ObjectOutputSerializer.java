package org.ee.serialization.serialization.serializable.output;

import org.ee.serialization.Config;
import org.ee.serialization.Serializer;
import org.ee.serialization.deserialization.serializable.mapper.model.CachingObjectOutput;

public interface ObjectOutputSerializer extends Serializer, CachingObjectOutput {
	Config getConfig();

	StreamBuffer getBlockDataBuffer();
}
