package org.ee.serialization.serialization.serializable.mapper.standard;

import java.io.IOException;
import java.io.ObjectStreamConstants;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.ee.serialization.serialization.serializable.ObjectOutputStreamSerializer;
import org.ee.serialization.serialization.serializable.mapper.ClassDescriptionManager;
import org.ee.serialization.serialization.serializable.mapper.SerializableMapper;
import org.ee.serialization.serialization.serializable.output.ObjectOutputSerializer;

public class HashMapMapper implements SerializableMapper {
	private static final Method CAPACITY;
	private static final Method LOAD_FACTOR;
	static {
		CAPACITY = getMethod("capacity");
		LOAD_FACTOR = getMethod("loadFactor");
	}
	private final ClassDescriptionManager cache;

	private static Method getMethod(String name) {
		try {
			Method capacity = HashMap.class.getDeclaredMethod(name);
			capacity.setAccessible(true);
			return capacity;
		} catch (NoSuchMethodException | SecurityException e) {
			return null;
		}
	}

	public HashMapMapper(ClassDescriptionManager cache) {
		this.cache = cache;
	}

	@Override
	public boolean canMap(Object object) {
		return object instanceof HashMap;
	}

	@Override
	public void map(Object object, ObjectOutputSerializer output) throws IOException {
		HashMap<?, ?> map = (HashMap<?, ?>) object;
		cache.writeFromDescription(object, output);
		ObjectOutputStreamSerializer.writeBlockDataHeader(output, 2 * Integer.SIZE / Byte.SIZE);
		output.writeInt(getCapacity(map));
		output.writeInt(map.size());
		for(Map.Entry<?, ?> entry : map.entrySet()) {
			output.writeObject(entry.getKey());
			output.writeObject(entry.getValue());
		}
		output.writeByte(ObjectStreamConstants.TC_ENDBLOCKDATA);
	}

	static int getCapacity(HashMap<?, ?> map) {
		try {
			return (Integer) CAPACITY.invoke(map);
		} catch (IllegalAccessException | InvocationTargetException e) {
			return map.size();
		}
	}

	static float getLoadFactor(HashMap<?, ?> map) {
		try {
			return (Float) LOAD_FACTOR.invoke(map);
		} catch (IllegalAccessException | InvocationTargetException e) {
			return map.size();
		}
	}
}
