package org.ee.serialization.serialization.serializable.output;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class StreamBufferPool {
	private static final int MAX_POOL_SIZE = 5;
	private final Deque<WeakReference<StreamBuffer>> bufferPool;
	private final OutputStream output;
	private final ObjectOutputSerializer serializer;

	public StreamBufferPool(OutputStream output, ObjectOutputSerializer serializer) {
		bufferPool = new LinkedList<>();
		this.output = output;
		this.serializer = serializer;
	}

	public StreamBuffer getStreamBuffer() {
		StreamBuffer buffer = null;
		for(Iterator<WeakReference<StreamBuffer>> it = bufferPool.iterator(); buffer == null && it.hasNext();) {
			WeakReference<StreamBuffer> ref = it.next();
			buffer = ref.get();
			it.remove();
		}
		if(buffer == null) {
			buffer = new StreamBuffer(this, serializer);
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
