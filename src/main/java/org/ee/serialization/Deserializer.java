package org.ee.serialization;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.ee.serialization.Config.Key;
import org.ee.serialization.deserialization.DelegateFactory;
import org.ee.serialization.deserialization.DelegateManager;
import org.ee.serialization.deserialization.DeserializationDelegate;

public class Deserializer implements Closeable {
	private static final Key<DelegateFactory> FACTORY = new Key<>();
	private final DeserializationDelegate input;

	public Deserializer(InputStream input) throws IOException {
		this(input, new Config());
	}

	public Deserializer(InputStream input, Config config) throws IOException {
		DelegateFactory factory = config.get(FACTORY);
		if(factory == null) {
			factory = DelegateManager.getInstance();
		}
		this.input = factory.createDeserializer(input, config);
	}

	public Object readObject() throws IOException, ClassNotFoundException {
		return input.readObject();
	}

	@Override
	public void close() throws IOException {
		input.close();
	}
}
