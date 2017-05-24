package org.ee.serialization.deserialization.serializable.mapper.platform;

import java.io.ObjectStreamClass;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

import org.ee.serialization.SerializationException;
import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescription;

public class JreConstructor implements NativeConstructor {
	private final Map<ClassDescription, Constructor<?>> cache;
	private final Method getSerializableConstructor;

	public JreConstructor() {
		try {
			getSerializableConstructor = ObjectStreamClass.class.getDeclaredMethod("getSerializableConstructor", Class.class);
			getSerializableConstructor.setAccessible(true);
		} catch (Exception e) {
			throw new UnsupportedOperationException("Not running an Oracle JRE", e);
		}
		cache = new WeakHashMap<>();
	}

	@Override
	public Object newInstance(ClassDescription description) throws SerializationException, ReflectiveOperationException, IllegalArgumentException {
		Constructor<?> constructor = cache.get(description);
		if(constructor == null) {
			Class<?> type = description.getType();
			constructor = (Constructor<?>) getSerializableConstructor.invoke(null, type);
			if(constructor == null) {
				throw new SerializationException("Failed to invoke constructor");
			}
			cache.put(description, constructor);
		}
		return constructor.newInstance();
	}
}
