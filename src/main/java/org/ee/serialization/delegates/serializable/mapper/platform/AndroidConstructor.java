package org.ee.serialization.delegates.serializable.mapper.platform;

import java.io.ObjectStreamClass;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

import org.ee.serialization.SerializationException;
import org.ee.serialization.delegates.serializable.mapper.model.ClassDescription;

public class AndroidConstructor implements NativeConstructor {
	private final Map<ClassDescription, ObjectStreamClass> cache;
	private final Method newInstance;
	private final Method setFlags;
	private final Method setClass;
	private final Constructor<ObjectStreamClass> constructor;

	public AndroidConstructor() {
		try {
			newInstance = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class);
			newInstance.setAccessible(true);
			setFlags = ObjectStreamClass.class.getDeclaredMethod("setFlags", byte.class);
			setFlags.setAccessible(true);
			setClass = ObjectStreamClass.class.getDeclaredMethod("setClass", Class.class);
			setClass.setAccessible(true);
			constructor = ObjectStreamClass.class.getDeclaredConstructor();
			constructor.setAccessible(true);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new UnsupportedOperationException("Not running on Android", e);
		}
		cache = new WeakHashMap<>();
	}

	@Override
	public Object newInstance(ClassDescription description) throws SerializationException, ReflectiveOperationException, IllegalArgumentException {
		ObjectStreamClass objectStreamClass = cache.get(description);
		if(objectStreamClass == null) {
			objectStreamClass = constructor.newInstance();
			setFlags.invoke(objectStreamClass, description.getInfo().getFlags());
			setClass.invoke(objectStreamClass, Class.forName(description.getName()));
			cache.put(description, objectStreamClass);
		}
		return newInstance.invoke(objectStreamClass, objectStreamClass.forClass());
	}
}
