package org.ee.serialization.serialization.serializable.mapper.standard;

import java.util.Arrays;

import org.ee.serialization.serialization.DelegatingMapper;
import org.ee.serialization.serialization.serializable.mapper.ClassMapper;
import org.ee.serialization.serialization.serializable.mapper.SerializableMapper;
import org.ee.serialization.serialization.serializable.output.SerializableDataOutputStream;

public class StandardMapper extends DelegatingMapper<SerializableDataOutputStream, SerializableMapper> implements SerializableMapper {
	public StandardMapper(ClassMapper cache) {
		super(Arrays.asList(new DateMapper(cache), new ArrayListMapper(cache)));
	}
}
