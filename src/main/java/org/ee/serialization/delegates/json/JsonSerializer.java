package org.ee.serialization.delegates.json;

import java.io.IOException;
import java.io.OutputStream;

import org.ee.serialization.Config;
import org.ee.serialization.Config.Key;
import org.ee.serialization.delegates.json.output.DefaultJsonDataOutputStreamFactory;
import org.ee.serialization.delegates.json.output.JsonDataOutputStream;
import org.ee.serialization.delegates.json.output.JsonDataOutputStreamFactory;
import org.ee.serialization.Serializer;

public class JsonSerializer implements Serializer {
	private static final Key<JsonDataOutputStreamFactory> STREAM_FACTORY = new Key<>();
	private final JsonDataOutputStream output;

	public JsonSerializer(OutputStream output, Config config) throws IOException {
		JsonDataOutputStreamFactory factory = config.getFactorySetting(STREAM_FACTORY);
		if(factory == null) {
			factory = DefaultJsonDataOutputStreamFactory.INSTANCE;
		}
		this.output = factory.createJsonDataOutputStream(output, config);
	}

	@Override
	public void close() throws IOException {
		output.close();
	}

	@Override
	public void writeObject(Object object) throws IOException {
		output.writeObject(object);
	}
}
