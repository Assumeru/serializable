package org.ee.serialization.serialization.json.mapper.standard;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ee.serialization.serialization.json.mapper.JsonMapper;
import org.ee.serialization.serialization.json.output.JsonDataOutputStream;

public class DefaultMapper implements JsonMapper {
	public static final DefaultMapper INSTANCE = new DefaultMapper();
	private final List<JsonMapper> mappers;

	public DefaultMapper() {
		mappers = Arrays.asList(new ArrayMapper(), new PrimitiveMapper(), new EnumMapper(), new ObjectMapper());
	}

	@Override
	public void map(Object object, JsonDataOutputStream output) throws IOException {
		for(JsonMapper mapper : mappers) {
			if(mapper.canMap(object)) {
				mapper.map(object, output);
				return;
			}
		}
	}

	@Override
	public boolean canMap(Object object) {
		for(JsonMapper mapper : mappers) {
			if(mapper.canMap(object)) {
				return true;
			}
		}
		return false;
	}
}
