package org.ee.serialization.serialization.json.output;

import java.io.IOException;
import java.io.OutputStream;

import org.ee.serialization.Config;
import org.ee.serialization.Config.Key;
import org.ee.serialization.serialization.ObjectFilter;
import org.ee.serialization.serialization.json.mapper.JsonMapper;
import org.ee.serialization.serialization.json.mapper.standard.DefaultMapper;
import org.ee.serialization.serialization.json.output.writer.GsonWriter;
import org.ee.serialization.serialization.json.output.writer.JsonWriter;
import org.ee.serialization.serialization.json.output.writer.JsonWriterFactory;

public class DefaultJsonDataOutputStreamFactory implements JsonDataOutputStreamFactory {
	public static final DefaultJsonDataOutputStreamFactory INSTANCE = new DefaultJsonDataOutputStreamFactory();
	public static final Key<JsonWriterFactory> WRITER_FACTORY = new Key<>();
	public static final Key<Boolean> PRETTY_PRINT = new Key<>();
	public static final Key<JsonMapper> JSON_MAPPER = new Key<>();
	public static final Key<ObjectFilter> OBJECT_FILTER = new Key<>();

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
		JsonMapper mapper = config.getFactorySetting(JSON_MAPPER);
		if(mapper == null) {
			mapper = DefaultMapper.INSTANCE;
		}
		return new DefaultJsonDataOutputStream(writer, config, mapper, config.getFactorySetting(OBJECT_FILTER));
	}

	private boolean getBoolean(Boolean value) {
		return value == null ? false : value;
	}
}
