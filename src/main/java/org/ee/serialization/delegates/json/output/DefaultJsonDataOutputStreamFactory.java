package org.ee.serialization.delegates.json.output;

import java.io.IOException;
import java.io.OutputStream;

import org.ee.serialization.Config;
import org.ee.serialization.Config.Key;
import org.ee.serialization.delegates.json.output.writer.GsonWriter;
import org.ee.serialization.delegates.json.output.writer.JsonWriter;
import org.ee.serialization.delegates.json.output.writer.JsonWriterFactory;

public class DefaultJsonDataOutputStreamFactory implements JsonDataOutputStreamFactory {
	public static final DefaultJsonDataOutputStreamFactory INSTANCE = new DefaultJsonDataOutputStreamFactory();
	private static final Key<JsonWriterFactory> WRITER_FACTORY = new Key<>();

	@Override
	public JsonDataOutputStream createJsonDataOutputStream(OutputStream output, Config config) throws IOException {
		JsonWriterFactory factory = config.getFactorySetting(WRITER_FACTORY);
		JsonWriter writer = null;
		if(factory != null) {
			writer = factory.createWriter(output, config);
		}
		if(writer == null) {
			writer = new GsonWriter(output);
		}
		return new DefaultJsonDataOutputStream(writer);
	}
}
