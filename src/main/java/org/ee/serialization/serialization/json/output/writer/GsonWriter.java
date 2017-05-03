package org.ee.serialization.serialization.json.output.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class GsonWriter implements JsonWriter {
	private final com.google.gson.stream.JsonWriter writer;

	public GsonWriter(com.google.gson.stream.JsonWriter writer) {
		this.writer = writer;
	}

	public GsonWriter(Writer writer) {
		this(new com.google.gson.stream.JsonWriter(writer));
	}

	public GsonWriter(OutputStream output) {
		this(new BufferedWriter(new OutputStreamWriter(output)));
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

	@Override
	public JsonWriter beginArray() throws IOException {
		writer.beginArray();
		return this;
	}

	@Override
	public JsonWriter beginObject() throws IOException {
		writer.beginObject();
		return this;
	}

	@Override
	public JsonWriter endArray() throws IOException {
		writer.endArray();
		return this;
	}

	@Override
	public JsonWriter endObject() throws IOException {
		writer.endObject();
		return this;
	}

	@Override
	public JsonWriter name(String name) throws IOException {
		writer.name(name);
		return this;
	}

	@Override
	public JsonWriter nullValue() throws IOException {
		writer.nullValue();
		return this;
	}

	@Override
	public JsonWriter value(boolean value) throws IOException {
		writer.value(value);
		return this;
	}

	@Override
	public JsonWriter value(double value) throws IOException {
		writer.value(value);
		return this;
	}

	@Override
	public JsonWriter value(Boolean value) throws IOException {
		writer.value(value);
		return this;
	}

	@Override
	public JsonWriter value(Number value) throws IOException {
		writer.value(value);
		return this;
	}

	@Override
	public JsonWriter value(String value) throws IOException {
		writer.value(value);
		return this;
	}

	@Override
	public JsonWriter value(long value) throws IOException {
		writer.value(value);
		return this;
	}

	public void setPrettyPrint(boolean on) {
		if(on) {
			writer.setIndent("\t");
		} else {
			writer.setIndent("");
		}
	}
}
