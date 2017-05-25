package org.ee.serialization.serialization.serializable.output;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class StreamBuffer extends ByteArrayOutputStream {
	private final SerializableDataOutputStream pool;
	private boolean closed;

	StreamBuffer(SerializableDataOutputStream pool) {
		this.pool = pool;
	}

	void open() {
		this.closed = false;
	}

	@Override
	public void close() throws IOException {
		if(!closed) {
			pool.returnBuffer(this);
			closed = true;
		}
	}
}
