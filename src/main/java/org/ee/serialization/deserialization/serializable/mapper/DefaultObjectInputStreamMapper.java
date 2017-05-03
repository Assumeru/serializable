package org.ee.serialization.deserialization.serializable.mapper;

public class DefaultObjectInputStreamMapper extends ObjectInputStreamDelegatingMapper {
	public DefaultObjectInputStreamMapper() {
		add(new ArrayMapper());
		add(new StandardObjectInputStreamMapper());
		add(new SerializableMapper());
		add(new ExternalizableMapper());
	}
}
