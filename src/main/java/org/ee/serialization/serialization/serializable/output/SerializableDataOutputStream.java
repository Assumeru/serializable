package org.ee.serialization.serialization.serializable.output;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectStreamConstants;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import org.ee.serialization.Config;
import org.ee.serialization.SerializationException;
import org.ee.serialization.Serializer;
import org.ee.serialization.serialization.CachingSerializer;
import org.ee.serialization.serialization.ObjectFilter;
import org.ee.serialization.serialization.serializable.mapper.SerializableMapper;

public class SerializableDataOutputStream extends CachingSerializer implements Serializer, ObjectOutput {
	private static final int MAX_POOL_SIZE = 5;
	private final Config config;
	private final DataOutputStream output;
	private final SerializableMapper mapper;
	private final ObjectFilter filter;
	private final Deque<WeakReference<StreamBuffer>> bufferPool;

	public SerializableDataOutputStream(OutputStream output, Config config, SerializableMapper mapper, ObjectFilter filter) throws IOException {
		this.output = output instanceof DataOutputStream ? (DataOutputStream) output : new DataOutputStream(output);
		this.config = config;
		this.mapper = mapper;
		this.filter = filter;
		bufferPool = new LinkedList<>();
		writeShort(ObjectStreamConstants.STREAM_MAGIC);
		writeShort(ObjectStreamConstants.STREAM_VERSION);
	}

	@Override
	protected void writeObjectOrReference(Object object) throws IOException {
		if(filter != null) {
			object = filter.filter(object, config);
		}
		if(mapper.canMap(object)) {
			mapper.map(object, this);
		} else {
			throw new SerializationException("Could not map " + object);
		}
	}

	@Override
	protected boolean shouldCache(Object object) {
		return  object.getClass() == String.class || super.shouldCache(object);
	}

	public Config getConfig() {
		return config;
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

	public StreamBuffer getStreamBuffer() {
		StreamBuffer buffer = null;
		for(Iterator<WeakReference<StreamBuffer>> it = bufferPool.iterator(); buffer == null && it.hasNext();) {
			WeakReference<StreamBuffer> ref = it.next();
			buffer = ref.get();
			it.remove();
		}
		if(buffer == null) {
			buffer = new StreamBuffer(this);
		}
		buffer.open();
		return buffer;
	}

	void returnBuffer(StreamBuffer buffer) throws IOException {
		buffer.writeTo(output);
		boolean add = bufferPool.size() < MAX_POOL_SIZE;
		if(!add) {
			for(Iterator<WeakReference<StreamBuffer>> it = bufferPool.iterator(); it.hasNext();) {
				WeakReference<StreamBuffer> ref = it.next();
				if(ref.get() == null) {
					it.remove();
					add = true;
				}
			}
		}
		if(add) {
			buffer.reset();
			bufferPool.add(new WeakReference<>(buffer));
		}
	}
}
