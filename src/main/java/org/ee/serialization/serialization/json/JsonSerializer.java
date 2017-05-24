package org.ee.serialization.serialization.json;

import java.io.IOException;
import java.io.OutputStream;

import org.ee.serialization.Config;
import org.ee.serialization.Config.Key;
import org.ee.serialization.Serializer;
import org.ee.serialization.serialization.json.mapper.DefaultMapper;
import org.ee.serialization.serialization.json.mapper.JsonMapper;
import org.ee.serialization.serialization.json.output.JsonDataOutputStream;
import org.ee.serialization.serialization.json.output.writer.GsonWriter;
import org.ee.serialization.serialization.json.output.writer.JsonWriter;
import org.ee.serialization.serialization.json.output.writer.JsonWriterFactory;

public class JsonSerializer implements Serializer {
	public static final Key<JsonWriterFactory> WRITER_FACTORY = new Key<>();
	public static final Key<Boolean> PRETTY_PRINT = new Key<>();
	public static final Key<JsonMapper> JSON_MAPPER = new Key<>();
	private final JsonDataOutputStream output;

	public JsonSerializer(OutputStream output, Config config) throws IOException {
		JsonWriterFactory factory = config.get(WRITER_FACTORY);
		JsonWriter writer = null;
		if(factory != null) {
			writer = factory.createWriter(output, config);
		}
		if(writer == null) {
			writer = new GsonWriter(output);
			((GsonWriter) writer).setPrettyPrint(getBoolean(config.get(PRETTY_PRINT)));
		}
		JsonMapper mapper = config.get(JSON_MAPPER);
		if(mapper == null) {
			mapper = DefaultMapper.INSTANCE;
		}
		this.output = new JsonDataOutputStream(writer, config, mapper, config.get(OBJECT_FILTER));
	}

	private boolean getBoolean(Boolean value) {
		return value == null ? false : value;
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
