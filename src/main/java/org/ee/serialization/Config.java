package org.ee.serialization;

import java.util.HashMap;
import java.util.Map;

import org.ee.serialization.delegates.DelegateFactory;
import org.ee.serialization.delegates.DelegateManager;

public class Config {
	private final Map<Key<?>, Object> factorySettings = new HashMap<>();
	private DelegateFactory factory;

	public Config setFactory(DelegateFactory factory) {
		this.factory = factory;
		return this;
	}

	public DelegateFactory getFactory() {
		if(factory == null) {
			return DelegateManager.getInstance();
		}
		return factory;
	}

	@SuppressWarnings("unchecked")
	public <E> E getFactorySetting(Key<E> key) {
		return (E) factorySettings.get(key);
	}

	public <E> Config setFactorySetting(Key<E> key, E value) {
		factorySettings.put(key, value);
		return this;
	}

	public static class Key<E> {
		
	}
}
