package org.ee.serialization.delegates.serializable.mapper.standard;

import java.io.IOException;

import org.ee.serialization.delegates.serializable.mapper.ObjectInputStreamMapper;
import org.ee.serialization.delegates.serializable.mapper.model.ObjectMapping;

public abstract class AbstractMapper<T> implements SingleClassMapper<T> {
	private final Class<T> type;

	public AbstractMapper(Class<T> type) {
		this.type = type;
	}

	@Override
	public T map(Object object, ObjectInputStreamMapper mapper) throws IOException, ClassNotFoundException {
		return doMap((ObjectMapping) object, mapper);
	}

	protected abstract T doMap(ObjectMapping object, ObjectInputStreamMapper mapper) throws IOException;

	@Override
	public boolean canMap(Object object) {
		if(object instanceof ObjectMapping) {
			return type.getName().equals(((ObjectMapping) object).getDescription().getName());
		}
		return false;
	}

	@Override
	public Class<T> getType() {
		return type;
	}
}
