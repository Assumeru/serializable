package org.ee.serialization.serialization.json.output;

import java.io.IOException;

import org.ee.serialization.Serializer;
import org.ee.serialization.serialization.json.output.writer.JsonWriter;

public interface JsonDataOutputStream extends Serializer, JsonWriter {
	public static final String PROPERTY_CLASS = "class";
	public static final String PROPERTY_FIELDS = "fields";
	public static final String PROPERTY_VERSION = "serialVersionUID";
	public static final String PROPERTY_DATA = "data";
	public static final String PROPERTY_ENUM_VALUE = "value";
	public static final String PROPERTY_ARRAY_VALUES = "values";

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
