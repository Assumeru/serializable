package org.ee.serialization.delegates.json.output;

import java.io.IOException;

import org.ee.serialization.Serializer;
import org.ee.serialization.delegates.json.output.writer.JsonWriter;

public interface JsonDataOutputStream extends Serializer, JsonWriter {
	@Override
	JsonDataOutputStream beginArray() throws IOException;

	@Override
	JsonDataOutputStream beginObject() throws IOException;

	@Override
	JsonDataOutputStream endArray() throws IOException;

	@Override
	JsonDataOutputStream endObject() throws IOException;

	@Override
	JsonDataOutputStream name(String name) throws IOException;

	@Override
	JsonDataOutputStream nullValue() throws IOException;

	@Override
	JsonDataOutputStream value(Boolean value) throws IOException;

	@Override
	JsonDataOutputStream value(boolean value) throws IOException;

	@Override
	JsonDataOutputStream value(double value) throws IOException;

	@Override
	JsonDataOutputStream value(long value) throws IOException;

	@Override
	JsonDataOutputStream value(Number value) throws IOException;

	@Override
	JsonDataOutputStream value(String value) throws IOException;
}
