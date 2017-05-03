package org.ee.serialization;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.ee.serialization.deserialization.DeserializationDelegate;

public class Deserializer implements Closeable {
	private final DeserializationDelegate input;

	public Deserializer(InputStream input) throws IOException {
		this(input, new Config());
	}

	public Deserializer(InputStream input, Config config) throws IOException {
		this.input = config.getFactory().createDeserializer(input, config);
	}

	public Object readObject() throws IOException, ClassNotFoundException {
		return input.readObject();
	}

	@Override
	public void close() throws IOException {
		input.close();
	}
}
