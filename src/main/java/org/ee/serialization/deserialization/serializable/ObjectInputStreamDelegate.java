package org.ee.serialization.deserialization.serializable;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.ee.serialization.deserialization.DeserializationDelegate;

public class ObjectInputStreamDelegate implements DeserializationDelegate {
	private final ObjectInputStream input;

	public ObjectInputStreamDelegate(InputStream input) throws IOException {
		this.input = new ObjectInputStream(input);
	}

	@Override
	public void close() throws IOException {
		input.close();
	}

	@Override
	public Object readObject() throws IOException, ClassNotFoundException {
		return input.readObject();
	}
}
