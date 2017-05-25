package org.ee.serialization.serialization.serializable.output;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.ee.serialization.Config;

public class StreamBuffer extends DataOutputStream implements ObjectOutputSerializer {
	private final ByteArrayOutputStream buffer;
	private final ObjectOutputSerializer serializer;
	private final StreamBufferPool parentPool;
	private final StreamBufferPool pool;
	private boolean closed;

	StreamBuffer(StreamBufferPool pool, ObjectOutputSerializer serializer) {
		this(pool, serializer, new ByteArrayOutputStream());
	}

	private StreamBuffer(StreamBufferPool pool, ObjectOutputSerializer serializer, ByteArrayOutputStream buffer) {
		super(buffer);
		this.buffer = buffer;
		this.parentPool = pool;
		this.serializer = serializer;
		this.pool = new StreamBufferPool(this, this);
	}

	@Override
	public void writeObject(Object object) throws IOException {
		serializer.writeObject(object, this);
	}

	@Override
	public void writeObject(Object object, ObjectOutputSerializer serializer) throws IOException {
		serializer.writeObject(object, serializer);
	}

	@Override
	public Config getConfig() {
		return serializer.getConfig();
	}

	@Override
	public StreamBuffer getStreamBuffer() {
		return pool.getStreamBuffer();
	}

	void open() {
		this.closed = false;
	}

	void reset() {
		written = 0;
        buffer.reset();
    }

	void writeTo(OutputStream output) throws IOException {
		buffer.writeTo(output);
	}

	@Override
	public void close() throws IOException {
		if(!closed) {
			parentPool.returnBuffer(this);
			closed = true;
		}
	}
}
