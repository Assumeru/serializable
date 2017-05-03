package org.ee.serialization.deserialization.serializable.mapper.model;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInput;
import java.util.List;

import org.ee.serialization.SerializationException;
import org.ee.serialization.deserialization.serializable.mapper.ObjectInputStreamMapper;

public class ListObjectInput implements ObjectInput {
	private static final byte[] EMPTY = {};
	private final ObjectInputStreamMapper mapper;
	private final List<Object> data;
	private final Buffer buffer;
	private final DataInputStream input;
	private int index;

	public ListObjectInput(List<Object> data, ObjectInputStreamMapper mapper) {
		this.mapper = mapper;
		this.data = data;
		buffer = new Buffer(EMPTY);
		input = new DataInputStream(buffer);
		index = -1;
		next();
	}

	private void next() {
		byte[] buf = EMPTY;
		index++;
		while(index < data.size() && data.get(index) instanceof byte[]) {
			byte[] dataBuffer = (byte[]) data.get(index);
			byte[] newBuffer = new byte[buf.length + dataBuffer.length];
			System.arraycopy(buf, 0, newBuffer, 0, buf.length);
			System.arraycopy(dataBuffer, 0, newBuffer, buf.length, dataBuffer.length);
			buf = newBuffer;
			index++;
		}
		buffer.setBuffer(buf);
	}

	@Override
	public void readFully(byte[] b) throws IOException {
		input.readFully(b);
	}

	@Override
	public void readFully(byte[] b, int off, int len) throws IOException {
		input.readFully(b, off, len);
	}

	@Override
	public int skipBytes(int n) throws IOException {
		return input.skipBytes(n);
	}

	@Override
	public boolean readBoolean() throws IOException {
		return input.readBoolean();
	}

	@Override
	public byte readByte() throws IOException {
		return input.readByte();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return input.readUnsignedByte();
	}

	@Override
	public short readShort() throws IOException {
		return input.readShort();
	}

	@Override
	public int readUnsignedShort() throws IOException {
		return input.readUnsignedShort();
	}

	@Override
	public char readChar() throws IOException {
		return input.readChar();
	}

	@Override
	public int readInt() throws IOException {
		return input.readInt();
	}

	@Override
	public long readLong() throws IOException {
		return input.readLong();
	}

	@Override
	public float readFloat() throws IOException {
		return input.readFloat();
	}

	@Override
	public double readDouble() throws IOException {
		return input.readDouble();
	}

	@SuppressWarnings("deprecation")
	@Override
	public String readLine() throws IOException {
		return input.readLine();
	}

	@Override
	public String readUTF() throws IOException {
		return input.readUTF();
	}

	@Override
	public Object readObject() throws ClassNotFoundException, IOException {
		if(data.size() <= index) {
			throw new EOFException();
		} else if(buffer.available() > 0) {
			throw new SerializationException("Not finished reading binary data");
		}
		Object o = data.get(index);
		next();
		if(mapper != null) {
			return mapper.map(o, mapper);
		}
		return o;
	}

	@Override
	public int read() throws IOException {
		return input.read();
	}

	@Override
	public int read(byte[] b) throws IOException {
		return input.read(b);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return input.read(b, off, len);
	}

	@Override
	public long skip(long n) throws IOException {
		return input.skip(n);
	}

	@Override
	public int available() throws IOException {
		return input.available();
	}

	@Override
	public void close() throws IOException {
	}

	private static class Buffer extends ByteArrayInputStream {
		public Buffer(byte[] buf) {
			super(buf);
		}

		void setBuffer(byte[] buf) {
			this.buf = buf;
			pos = 0;
			count = buf.length;
		}
	}
}
