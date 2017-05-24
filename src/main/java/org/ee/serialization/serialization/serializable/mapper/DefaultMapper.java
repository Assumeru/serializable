package org.ee.serialization.serialization.serializable.mapper;

import java.util.Arrays;

import org.ee.serialization.serialization.DelegatingMapper;
import org.ee.serialization.serialization.serializable.output.SerializableDataOutputStream;

public class DefaultMapper extends DelegatingMapper<SerializableDataOutputStream> implements SerializableMapper {
	public static final DefaultMapper INSTANCE = new DefaultMapper();

	public DefaultMapper() {
		super(Arrays.asList(new PrimitiveMapper(), new ReferenceMapper(), new ObjectMapper()));
	}
}
