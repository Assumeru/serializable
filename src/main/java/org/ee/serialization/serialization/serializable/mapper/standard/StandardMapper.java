package org.ee.serialization.serialization.serializable.mapper.standard;

import java.util.Arrays;

import org.ee.serialization.serialization.DelegatingMapper;
import org.ee.serialization.serialization.serializable.mapper.ClassDescriptionManager;
import org.ee.serialization.serialization.serializable.mapper.SerializableMapper;
import org.ee.serialization.serialization.serializable.output.ObjectOutputSerializer;

public class StandardMapper extends DelegatingMapper<ObjectOutputSerializer, SerializableMapper> implements SerializableMapper {
	public StandardMapper(ClassDescriptionManager cache) {
		super(Arrays.asList(new DateMapper(cache), new ListMapper(cache), new HashMapMapper(cache), new HashSetMapper(cache)));
	}
}
