package org.ee.serialization.serialization.json.output;

import java.io.IOException;
import java.io.OutputStream;

import org.ee.serialization.Config;
import org.ee.serialization.Config.Key;
import org.ee.serialization.serialization.json.output.writer.GsonWriter;
import org.ee.serialization.serialization.json.output.writer.JsonWriter;
import org.ee.serialization.serialization.json.output.writer.JsonWriterFactory;

public class DefaultJsonDataOutputStreamFactory implements JsonDataOutputStreamFactory {
	public static final DefaultJsonDataOutputStreamFactory INSTANCE = new DefaultJsonDataOutputStreamFactory();
	public static final Key<JsonWriterFactory> WRITER_FACTORY = new Key<>();
	public static final Key<Boolean> ADD_VERSION = new Key<>();
	public static final Key<Boolean> PRETTY_PRINT = new Key<>();

	@Override
	public JsonDataOutputStream createJsonDataOutputStream(OutputStream output, Config config) throws IOException {
		JsonWriterFactory factory = config.getFactorySetting(WRITER_FACTORY);
		JsonWriter writer = null;
		if(factory != null) {
			writer = factory.createWriter(output, config);
		}
		if(writer == null) {
			writer = new GsonWriter(output);
			((GsonWriter) writer).setPrettyPrint(getBoolean(config.getFactorySetting(PRETTY_PRINT)));
		}
		return new DefaultJsonDataOutputStream(writer, getBoolean(config.getFactorySetting(ADD_VERSION)));
	}

	private boolean getBoolean(Boolean value) {
		return value == null ? false : value;
	}
}
