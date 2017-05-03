package org.ee.serialization.deserialization.serializable.mapper;

import java.io.IOException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObjectInputStreamDelegatingMapper extends AbstractCollection<ObjectInputStreamMapperDelegate> implements ObjectInputStreamMapper {
	private final List<ObjectInputStreamMapperDelegate> delegates;

	public ObjectInputStreamDelegatingMapper() {
		delegates = new ArrayList<>();
	}

	@Override
	public Object map(Object object, ObjectInputStreamMapper mapper) throws ClassNotFoundException, IOException {
		for(ObjectInputStreamMapperDelegate delegate : delegates) {
			if(delegate.canMap(object)) {
				return delegate.map(object, mapper);
			}
		}
		return object;
	}

	@Override
	public Iterator<ObjectInputStreamMapperDelegate> iterator() {
		return delegates.iterator();
	}

	@Override
	public int size() {
		return delegates.size();
	}

	@Override
	public boolean add(ObjectInputStreamMapperDelegate e) {
		return delegates.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return delegates.remove(o);
	}
}
