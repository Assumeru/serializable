package org.ee.serialization.serialization.serializable;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectStreamConstants;
import java.io.OutputStream;

import org.ee.serialization.Config;
import org.ee.serialization.Config.Key;
import org.ee.serialization.Serializer;
import org.ee.serialization.serialization.serializable.mapper.DefaultMapper;
import org.ee.serialization.serialization.serializable.mapper.SerializableMapper;
import org.ee.serialization.serialization.serializable.output.SerializableDataOutputStream;

public class ObjectOutputStreamSerializer implements Serializer {
	public static final Key<SerializableMapper> SERIALIZABLE_MAPPER = new Key<>();
	private final SerializableDataOutputStream output;

	public ObjectOutputStreamSerializer(OutputStream output, Config config) throws IOException {
		this.output = new SerializableDataOutputStream(output, config, config.get(SERIALIZABLE_MAPPER, DefaultMapper.INSTANCE), config.get(OBJECT_FILTER));
	}

	@Override
	public void close() throws IOException {
		output.close();
	}

	@Override
	public void writeObject(Object object) throws IOException {
		output.writeObject(object);
	}

	public static void writeBlockData(ObjectOutput output, byte[] data) throws IOException {
		writeBlockDataHeader(output, data.length);
		output.write(data);
	}

	public static void writeBlockDataHeader(ObjectOutput output, int size) throws IOException {
		if(size > 0xFF) {
			output.writeByte(ObjectStreamConstants.TC_BLOCKDATALONG);
			output.writeInt(size);
		} else {
			output.writeByte(ObjectStreamConstants.TC_BLOCKDATA);
			output.writeByte(size);
		}
	}
}
