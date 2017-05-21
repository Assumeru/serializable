package org.ee.serialization.serialization.serializable.output;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectStreamConstants;
import java.io.OutputStream;

import org.ee.serialization.Config;
import org.ee.serialization.Serializer;

public class SerializableDataOutputStream extends DataOutputStream implements Serializer, ObjectOutput {
	private final Config config;

	public SerializableDataOutputStream(OutputStream output, Config config) throws IOException {
		super(output);
		this.config = config;
		writeShort(ObjectStreamConstants.STREAM_MAGIC);
		writeShort(ObjectStreamConstants.STREAM_VERSION);
	}

	@Override
	public void writeObject(Object object) throws IOException {
		// TODO
		
	}

	public Config getConfig() {
		return config;
	}
}
