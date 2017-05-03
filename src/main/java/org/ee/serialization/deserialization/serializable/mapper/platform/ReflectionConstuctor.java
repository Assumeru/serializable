package org.ee.serialization.deserialization.serializable.mapper.platform;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.WeakHashMap;

import org.ee.serialization.SerializationException;
import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescription;

public class ReflectionConstuctor implements NativeConstructor {
	private final Map<ClassDescription, Constructor<?>> cache = new WeakHashMap<>();

	@Override
	public Object newInstance(ClassDescription description) throws SerializationException, ReflectiveOperationException, IllegalArgumentException {
		Constructor<?> constructor = cache.get(description);
		if(constructor == null) {
			Class<?> type = Class.forName(description.getName());
			try {
				constructor = type.getDeclaredConstructor();
				constructor.setAccessible(true);
			} catch(NoSuchMethodException | SecurityException e) {
				throw new SerializationException("Failed to invoke constructor", e);
			}
			cache.put(description, constructor);
		}
		return constructor.newInstance();
	}
}
