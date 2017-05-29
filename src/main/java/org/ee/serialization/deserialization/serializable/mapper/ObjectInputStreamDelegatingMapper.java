package org.ee.serialization.deserialization.serializable.mapper;

import java.io.IOException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ObjectInputStreamDelegatingMapper extends AbstractCollection<ObjectInputStreamMapperDelegate> implements ObjectInputStreamMapper {
	private final List<ObjectInputStreamMapperDelegate> delegates;
	private final Map<Object, Object> cache;

	public ObjectInputStreamDelegatingMapper() {
		delegates = new ArrayList<>();
		cache = new IdentityHashMap<>();
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

	@Override
	public void cache(Object object, Object mapping) {
		if(object != null) {
			cache.put(object, mapping);
		}
	}

	@Override
	public Object getFromCache(Object object) {
		return cache.get(object);
	}
}
