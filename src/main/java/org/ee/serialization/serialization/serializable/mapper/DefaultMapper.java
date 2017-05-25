package org.ee.serialization.serialization.serializable.mapper;

import java.util.ArrayList;

import org.ee.serialization.serialization.DelegatingMapper;
import org.ee.serialization.serialization.serializable.mapper.standard.StandardMapper;
import org.ee.serialization.serialization.serializable.output.ObjectOutputSerializer;

public class DefaultMapper extends DelegatingMapper<ObjectOutputSerializer, SerializableMapper> implements SerializableMapper {
	public static final DefaultMapper INSTANCE = new DefaultMapper();

	public DefaultMapper() {
		super(new ArrayList<SerializableMapper>());
		ClassMapper cache = new ClassMapper();
		mappers.add(new PrimitiveMapper());
		mappers.add(new ReferenceMapper());
		mappers.add(new StandardMapper(cache));
		mappers.add(new ObjectMapper(cache));
	}
}
