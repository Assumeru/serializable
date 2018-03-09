package org.ee.serialization.serialization.serializable.output;

import org.ee.serialization.Config;
import org.ee.serialization.Serializer;

public interface ObjectOutputSerializer extends Serializer, CachingObjectOutput {
	Config getConfig();

	StreamBuffer getBlockDataBuffer();
}
