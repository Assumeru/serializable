package org.ee.serialization.serialization.serializable.output;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectStreamConstants;
import java.io.OutputStream;

import org.ee.serialization.Config;
import org.ee.serialization.SerializationException;
import org.ee.serialization.serialization.CachingSerializer;
import org.ee.serialization.serialization.ObjectFilter;
import org.ee.serialization.serialization.serializable.mapper.SerializableMapper;

public class SerializableDataOutputStream extends CachingSerializer implements ObjectOutputSerializer {
	private final Config config;
	private final DataOutputStream output;
	private final SerializableMapper mapper;
	private final ObjectFilter filter;
	private final StreamBufferPool pool;

	public SerializableDataOutputStream(OutputStream output, Config config, SerializableMapper mapper, ObjectFilter filter) throws IOException {
		super(false, config.get(USE_IDENTITY_COMPARE, true));
		this.output = output instanceof DataOutputStream ? (DataOutputStream) output : new DataOutputStream(output);
		this.config = config;
		this.mapper = mapper;
		this.filter = filter;
		pool = new StreamBufferPool(this.output, this);
		writeShort(ObjectStreamConstants.STREAM_MAGIC);
		writeShort(ObjectStreamConstants.STREAM_VERSION);
	}

	@Override
	public void writeObject(Object object, ObjectOutputSerializer serializer) throws IOException {
		writeObjectOrReference(getFromCache(object), serializer);
	}

	@Override
	protected void writeObjectOrReference(Object object) throws IOException {
		writeObjectOrReference(object, this);
	}

	private void writeObjectOrReference(Object object, ObjectOutputSerializer serializer) throws IOException {
		if(filter != null) {
			object = filter.filter(object, config);
		}
		if(mapper.canMap(object)) {
			mapper.map(object, serializer);
		} else {
			throw new SerializationException("Could not map " + object);
		}
	}

	@Override
	protected boolean shouldCache(Object object) {
		return object != null && (object.getClass() == String.class || super.shouldCache(object));
	}

	@Override
	public void assignHandle(Object object) {
		addToCache(object);
	}

	@Override
	public Config getConfig() {
		return config;
	}

	@Override
	public StreamBuffer getStreamBuffer() {
		return pool.getStreamBuffer();
	}

	@Override
	public void close() throws IOException {
		output.close();
	}

	@Override
	public void writeBoolean(boolean v) throws IOException {
		output.writeBoolean(v);
	}

	@Override
	public void writeByte(int v) throws IOException {
		output.writeByte(v);
	}

	@Override
	public void writeShort(int v) throws IOException {
		output.writeShort(v);
	}

	@Override
	public void writeChar(int v) throws IOException {
		output.writeChar(v);
	}

	@Override
	public void writeInt(int v) throws IOException {
		output.writeInt(v);
	}

	@Override
	public void writeLong(long v) throws IOException {
		output.writeLong(v);
	}

	@Override
	public void writeFloat(float v) throws IOException {
		output.writeFloat(v);
	}

	@Override
	public void writeDouble(double v) throws IOException {
		output.writeDouble(v);
	}

	@Override
	public void writeBytes(String s) throws IOException {
		output.writeBytes(s);
	}

	@Override
	public void writeChars(String s) throws IOException {
		output.writeChars(s);
	}

	@Override
	public void writeUTF(String s) throws IOException {
		output.writeUTF(s);
	}

	@Override
	public void write(int b) throws IOException {
		output.write(b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		output.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		output.write(b, off, len);
	}

	@Override
	public void flush() throws IOException {
		output.flush();
	}
}
