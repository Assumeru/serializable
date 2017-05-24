package org.ee.serialization.serialization.json.mapper;

import java.io.IOException;

import org.ee.serialization.serialization.json.output.JsonDataOutputStream;

public class PrimitiveMapper implements JsonMapper {
	@Override
	public void map(Object object, JsonDataOutputStream output) throws IOException {
		if(object == null) {
			output.value((String) null);
		} else if(object.getClass() == String.class || object.getClass() == Character.class) {
			output.value((String) object);
		} else if(object.getClass() == Boolean.class) {
			output.value((Boolean) object);
		} else if(object instanceof Number) {
			output.value((Number) object);
		}
	}

	@Override
	public boolean canMap(Object object) {
		return object == null || object.getClass() == String.class || object.getClass() == Boolean.class
				|| object.getClass() == Character.class || object instanceof Number;
	}
}
