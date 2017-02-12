package org.ee.serialization.delegates.serializable.mapper;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.ee.serialization.PeekInputStream;
import org.ee.serialization.SerializationException;

public class MapperDelegateInputStream extends DataInputStream {
	private final PeekInputStream input;

	public MapperDelegateInputStream(InputStream input) {
		super(new PeekInputStream(input));
		this.input = (PeekInputStream) in;
	}

	public int peek() throws IOException {
		return input.peek();
	}

	public String readLongUtf() throws IOException {
		long length = readLong();
		if(length > Integer.MAX_VALUE) {
			throw new SerializationException("String too long: " + length);
		}
		//TODO potentially incorrect
		return new String(read((int) length), "utf-8");
	}

	public byte[] read(int length) throws IOException {
		byte[] buffer = new byte[length];
		if(input.read(buffer) != length) {
			throw new EOFException("Failed to read " + length + " bytes");
		}
		return buffer;
	}
}
