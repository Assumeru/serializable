package org.ee.serialization.serialization.json.output;

import java.io.IOException;

import org.ee.serialization.Config;
import org.ee.serialization.SerializationException;
import org.ee.serialization.serialization.CachingSerializer;
import org.ee.serialization.serialization.ObjectFilter;
import org.ee.serialization.serialization.json.JsonSerializable;
import org.ee.serialization.serialization.json.mapper.JsonMapper;
import org.ee.serialization.serialization.json.output.writer.JsonWriter;

public class JsonDataOutputStream extends CachingSerializer implements JsonWriter {
	public static final String PROPERTY_CLASS = "class";
	public static final String PROPERTY_FIELDS = "fields";
	public static final String PROPERTY_VERSION = "serialVersionUID";
	public static final String PROPERTY_DATA = "data";
	public static final String PROPERTY_ENUM_VALUE = "value";
	public static final String PROPERTY_ARRAY_VALUES = "values";
	private final JsonWriter output;
	private final Config config;
	private final JsonMapper mapper;
	private final ObjectFilter filter;

	public JsonDataOutputStream(JsonWriter output, Config config, JsonMapper mapper, ObjectFilter filter) throws IOException {
		super(true, config.get(USE_IDENTITY_COMPARE, false));
		this.output = output;
		this.config = config;
		this.mapper = mapper;
		this.filter = filter;
	}

	@Override
	protected void writeObjectOrReference(Object object) throws IOException {
		if(filter != null) {
			object = filter.filter(object, config);
		}
		if(object instanceof JsonSerializable) {
			((JsonSerializable) object).toJson(this);
		} else if(mapper.canMap(object)) {
			mapper.map(object, this);
		} else {
			throw new SerializationException("Could not map " + object);
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

	public Config getConfig() {
		return config;
	}
}
