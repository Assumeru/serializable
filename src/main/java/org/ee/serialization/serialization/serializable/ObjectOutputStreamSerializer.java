package org.ee.serialization.serialization.serializable;

import java.io.IOException;
import java.io.OutputStream;

import org.ee.serialization.Config;
import org.ee.serialization.Serializer;
import org.ee.serialization.serialization.serializable.output.SerializableDataOutputStream;

public class ObjectOutputStreamSerializer implements Serializer {
	private final SerializableDataOutputStream output;

	public ObjectOutputStreamSerializer(OutputStream output, Config config) throws IOException {
		this.output = new SerializableDataOutputStream(output, config);
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
