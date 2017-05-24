package org.ee.serialization.serialization.json.mapper;

import java.util.Arrays;

import org.ee.serialization.serialization.DelegatingMapper;
import org.ee.serialization.serialization.json.output.JsonDataOutputStream;

public class DefaultMapper extends DelegatingMapper<JsonDataOutputStream> implements JsonMapper {
	public static final DefaultMapper INSTANCE = new DefaultMapper();

	public DefaultMapper() {
		super(Arrays.asList(new ArrayMapper(), new PrimitiveMapper(), new EnumMapper(), new ObjectMapper()));
	}
}
