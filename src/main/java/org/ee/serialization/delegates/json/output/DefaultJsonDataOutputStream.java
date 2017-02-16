package org.ee.serialization.delegates.json.output;

import java.io.IOException;

import org.ee.serialization.delegates.json.JsonSerializable;
import org.ee.serialization.delegates.json.output.writer.JsonWriter;

public class DefaultJsonDataOutputStream implements JsonDataOutputStream {
	private final JsonWriter output;

	public DefaultJsonDataOutputStream(JsonWriter output) throws IOException {
		this.output = output;
	}

	@Override
	public void writeObject(Object object) throws IOException {
		if(object instanceof JsonSerializable) {
			((JsonSerializable) object).toJson(this);
		} else {
			//TODO write json
		}
	}

	@Override
	public JsonDataOutputStream beginArray() throws IOException {
		output.beginArray();
		return this;
	}

	@Override
	public JsonDataOutputStream beginObject() throws IOException {
		output.beginObject();
		return this;
	}

	@Override
	public void close() throws IOException {
		output.close();
	}

	@Override
	public JsonDataOutputStream endArray() throws IOException {
		output.endArray();
		return this;
	}

	@Override
	public JsonDataOutputStream endObject() throws IOException {
		output.endObject();
		return this;
	}

	@Override
	public JsonDataOutputStream name(String name) throws IOException {
		output.name(name);
		return this;
	}

	@Override
	public JsonDataOutputStream nullValue() throws IOException {
		output.nullValue();
		return this;
	}

	@Override
	public JsonDataOutputStream value(boolean value) throws IOException {
		output.value(value);
		return this;
	}

	@Override
	public JsonDataOutputStream value(double value) throws IOException {
		output.value(value);
		return this;
	}

	@Override
	public JsonDataOutputStream value(Boolean value) throws IOException {
		output.value(value);
		return this;
	}

	@Override
	public JsonDataOutputStream value(Number value) throws IOException {
		output.value(value);
		return this;
	}

	@Override
	public JsonDataOutputStream value(String value) throws IOException {
		output.value(value);
		return this;
	}

	@Override
	public JsonDataOutputStream value(long value) throws IOException {
		output.value(value);
		return this;
	}
}
