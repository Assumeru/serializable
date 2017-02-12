package org.ee.serialization;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PeekInputStream extends FilterInputStream {
	private int peek;

	public PeekInputStream(InputStream in) {
		super(in);
		peek = -1;
	}

	public int peek() throws IOException {
		if(peek < 0) {
			peek = in.read();
		}
		return peek;
	}

	@Override
	public int read() throws IOException {
		if(peek < 0) {
			return in.read();
		}
		int out = peek;
		peek = -1;
		return out;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if(peek < 0) {
			return in.read(b, off, len);
		} else if(len > 0) {
			b[off] = (byte) peek;
			int read = in.read(b, off + 1, len - 1);
			return read > 0 ? read + 1 : 1;
		}
		return 0;
	}

	@Override
	public int read(byte[] b) throws IOException {
		if(peek < 0) {
			return in.read(b);
		} else if(b.length > 0) {
			b[0] = (byte) peek;
			peek = -1;
			int read = in.read(b, 1, b.length - 1);
			return read > 0 ? read + 1 : 1;
		}
		return 0;
	}

	@Override
	public long skip(long n) throws IOException {
		if(peek < 0) {
			return in.skip(n);
		} else if(n > 0) {
			peek = -1;
			return 1 + in.skip(n - 1);
		}
		return 0;
	}

	@Override
	public int available() throws IOException {
		if(peek < 0) {
			return in.available();
		}
		return 1 + in.available();
	}

	@Override
	public boolean markSupported() {
		return false;
	}
}