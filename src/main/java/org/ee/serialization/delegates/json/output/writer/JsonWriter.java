package org.ee.serialization.delegates.json.output.writer;

import java.io.Closeable;
import java.io.IOException;

/**
 * com.google.gson.stream.JsonWriter compatible interface
 */
public interface JsonWriter extends Closeable {
	JsonWriter beginArray() throws IOException;

	JsonWriter beginObject() throws IOException;

	JsonWriter endArray() throws IOException;

	JsonWriter endObject() throws IOException;

	JsonWriter name(String name) throws IOException;

	JsonWriter nullValue() throws IOException;

	JsonWriter value(boolean value) throws IOException;

	JsonWriter value(double value) throws IOException;

	JsonWriter value(Boolean value) throws IOException;

	JsonWriter value(Number value) throws IOException;

	JsonWriter value(String value) throws IOException;

	JsonWriter value(long value) throws IOException;
}
