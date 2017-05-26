package org.ee.serialization.deserialization.serializable.mapper;

public class DefaultObjectInputStreamMapper extends ObjectInputStreamDelegatingMapper {
	public DefaultObjectInputStreamMapper() {
		add(new EnumMapper());
		add(new ArrayMapper());
		add(new ClassMapper());
		add(new StandardObjectInputStreamMapper());
		add(new SerializableMapper());
		add(new ExternalizableMapper());
	}
}
