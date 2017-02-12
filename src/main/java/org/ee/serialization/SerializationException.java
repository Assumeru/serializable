package org.ee.serialization;

import java.io.IOException;

public class SerializationException extends IOException {
	private static final long serialVersionUID = -5063124890216122230L;

	public SerializationException() {
		super();
	}

	public SerializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public SerializationException(String message) {
		super(message);
	}

	public SerializationException(Throwable cause) {
		super(cause);
	}
}
