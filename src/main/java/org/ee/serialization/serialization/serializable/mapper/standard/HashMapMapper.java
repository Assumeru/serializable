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
	static {
		Method capacity;
		try {
			capacity = HashMap.class.getDeclaredMethod("capacity");
			capacity.setAccessible(true);
		} catch (NoSuchMethodException | SecurityException e) {
			capacity = null;
		}
		CAPACITY = capacity;
	}
	private final ClassDescriptionManager cache;

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

	private int getCapacity(HashMap<?, ?> map) {
		try {
			return (Integer) CAPACITY.invoke(map);
		} catch (IllegalAccessException | InvocationTargetException e) {
			return map.size();
		}
	}
}
