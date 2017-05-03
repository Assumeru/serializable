package org.ee.serialization;

import java.io.IOException;
import java.io.OutputStream;

public class GuardedOutputStream extends OutputStream {
	private final OutputStream output;

	public GuardedOutputStream(OutputStream output) {
		this.output = output;
	}

	@Override
	public void write(int b) throws IOException {
		output.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		output.write(b, off, len);
	}

	@Override
	public void close() throws IOException {
		flush();
	}

	@Override
	public void flush() throws IOException {
		output.flush();
	}

	@Override
	public void write(byte[] b) throws IOException {
		output.write(b);
	}
}
