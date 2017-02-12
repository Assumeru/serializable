package org.ee.serialization;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CountingInputStream extends FilterInputStream {
	private long count;
	private long mark;

	public CountingInputStream(InputStream in) {
		super(in);
	}

	@Override
	public int read() throws IOException {
		int out = super.read();
		if(out >= 0) {
			count++;
		}
		return out;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int out = super.read(b, off, len);
		if(out > 0) {
			count += out;
		}
		return out;
	}

	@Override
	public int read(byte[] b) throws IOException {
		int out = super.read(b);
		if(out > 0) {
			count += out;
		}
		return out;
	}

	@Override
	public synchronized void mark(int readlimit) {
		super.mark(readlimit);
		mark = count;
	}

	@Override
	public synchronized void reset() throws IOException {
		super.reset();
		count = mark;
	}

	public long getCount() {
		return count;
	}
}
