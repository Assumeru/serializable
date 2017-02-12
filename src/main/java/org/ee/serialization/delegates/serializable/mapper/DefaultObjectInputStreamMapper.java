package org.ee.serialization.delegates.serializable.mapper;

public class DefaultObjectInputStreamMapper extends ObjectInputStreamDelegatingMapper {
	public DefaultObjectInputStreamMapper() {
		add(new ArrayMapper());
		add(new StandardObjectInputStreamMapper());
		add(new SerializableMapper());
		add(new ExternalizableMapper());
	}
}
