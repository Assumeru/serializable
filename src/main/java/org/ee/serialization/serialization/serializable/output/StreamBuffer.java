package org.ee.serialization.serialization.serializable.output;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.ee.serialization.Config;
import org.ee.serialization.serialization.serializable.ObjectOutputStreamSerializer;

public class StreamBuffer extends DataOutputStream implements ObjectOutputSerializer {
	private static final int MAX_SIZE = 1024;
	private final ByteArrayOutputStream buffer;
	private final ObjectOutputSerializer serializer;
	private final StreamBufferPool parentPool;
	private final StreamBufferPool pool;
	private boolean closed;

	StreamBuffer(StreamBufferPool pool, ObjectOutputSerializer serializer) {
		this(pool, serializer, new ByteArrayOutputStream(MAX_SIZE));
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
		drain();
		serializer.writeObject(object);
	}

	@Override
	public void assignHandle(Object object) {
		serializer.assignHandle(object);
	}

	@Override
	public Config getConfig() {
		return serializer.getConfig();
	}

	@Override
	public StreamBuffer getBlockDataBuffer() {
		return pool.getStreamBuffer();
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		while(len > 0) {
			if(written > MAX_SIZE - len) {
				int write = MAX_SIZE - written;
				if(write > 0) {
					written += write;
					out.write(b, off, write);
					off += write;
					len -= write;
				}
				drain();
			} else {
				out.write(b, off, len);
				written += len;
				len = 0;
			}
		}
	}

	@Override
	public void write(int b) throws IOException {
		if(written > MAX_SIZE - 1) {
			drain();
		}
		out.write(b);
		written++;
	}

	@Override
	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}

	void open() {
		this.closed = false;
	}

	private void drain() throws IOException {
		int size = size();
		if(size > 0) {
			ObjectOutputStreamSerializer.writeBlockDataHeader(serializer, size);
			buffer.writeTo(parentPool.getOutput());
			written = 0;
	        buffer.reset();
		}
	}

	@Override
	public void close() throws IOException {
		if(!closed) {
			drain();
			parentPool.returnBuffer(this);
			closed = true;
		}
	}
}
