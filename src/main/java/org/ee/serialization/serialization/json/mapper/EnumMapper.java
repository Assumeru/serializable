package org.ee.serialization.serialization.json.mapper;

import java.io.IOException;

import org.ee.serialization.serialization.json.output.JsonDataOutputStream;

public class EnumMapper implements JsonMapper {
	@Override
	public void map(Object object, JsonDataOutputStream output) throws IOException {
		output.beginObject();
		output.name(JsonDataOutputStream.PROPERTY_CLASS).value(object.getClass().getName());
		output.name(JsonDataOutputStream.PROPERTY_ENUM_VALUE).value(((Enum<?>) object).name());
		output.endObject();
	}

	@Override
	public boolean canMap(Object object) {
		return object instanceof Enum;
	}
}
