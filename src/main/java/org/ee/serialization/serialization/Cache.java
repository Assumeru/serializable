package org.ee.serialization.serialization;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public class Cache extends AbstractMap<Object, Reference> {
	private static final Set<Class<?>> PRIMITIVES = new HashSet<>(Arrays.<Class<?>>asList(
			Boolean.class, Byte.class, Character.class, Double.class, Float.class,
			Integer.class, Long.class, Short.class, String.class));
	private final Map<Object, Reference> identity = new IdentityHashMap<>();
	private final Map<Object, Reference> equality = new HashMap<>();

	@Override
	public Set<java.util.Map.Entry<Object, Reference>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsKey(Object key) {
		return identity.containsKey(key) || equality.containsKey(key);
	}

	@Override
	public int size() {
		return identity.size() + equality.size();
	}

	@Override
	public Reference put(Object key, Reference value) {
		if(key == null || !PRIMITIVES.contains(key.getClass())) {
			return identity.put(key, value);
		}
		return equality.put(key, value);
	}

	@Override
	public Reference get(Object key) {
		if(identity.containsKey(key)) {
			return identity.get(key);
		}
		return equality.get(key);
	}
}
