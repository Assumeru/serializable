package org.ee.serialization;

import java.util.HashMap;
import java.util.Map;

public class Config {
	private final Map<Key<?>, Object> factorySettings = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <E> E get(Key<E> key) {
		return (E) factorySettings.get(key);
	}

	public <E> Config set(Key<E> key, E value) {
		factorySettings.put(key, value);
		return this;
	}

	public static final class Key<E> {
	}
}
