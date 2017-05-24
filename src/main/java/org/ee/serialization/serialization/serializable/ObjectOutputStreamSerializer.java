package org.ee.serialization.serialization.serializable;

import java.io.IOException;
import java.io.OutputStream;

import org.ee.serialization.Config;
import org.ee.serialization.Config.Key;
import org.ee.serialization.Serializer;
import org.ee.serialization.serialization.serializable.mapper.DefaultMapper;
import org.ee.serialization.serialization.serializable.mapper.SerializableMapper;
import org.ee.serialization.serialization.serializable.output.SerializableDataOutputStream;

public class ObjectOutputStreamSerializer implements Serializer {
	public static final Key<SerializableMapper> SERIALIZABLE_MAPPER = new Key<>();
	private final SerializableDataOutputStream output;

	public ObjectOutputStreamSerializer(OutputStream output, Config config) throws IOException {
		SerializableMapper mapper = config.get(SERIALIZABLE_MAPPER);
		if(mapper == null) {
			mapper = DefaultMapper.INSTANCE;
		}
		this.output = new SerializableDataOutputStream(output, config, mapper, config.get(OBJECT_FILTER));
	}

	@Override
	public void close() throws IOException {
		output.close();
	}

	@Override
	public void writeObject(Object object) throws IOException {
		output.writeObject(object);
	}
}
