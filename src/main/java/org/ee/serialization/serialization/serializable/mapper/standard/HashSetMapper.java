package org.ee.serialization.serialization.serializable.mapper.standard;

import java.io.IOException;
import java.io.ObjectStreamConstants;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;

import org.ee.serialization.serialization.serializable.mapper.ClassDescriptionManager;
import org.ee.serialization.serialization.serializable.mapper.SerializableMapper;
import org.ee.serialization.serialization.serializable.output.ObjectOutputSerializer;
import org.ee.serialization.serialization.serializable.output.StreamBuffer;

public class HashSetMapper implements SerializableMapper {
	private static final Field MAP;
	static {
		Field field;
		try {
			field = HashSet.class.getDeclaredField("map");
			field.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			field = null;
		}
		MAP = field;
	}
	private final ClassDescriptionManager cache;

	public HashSetMapper(ClassDescriptionManager cache) {
		this.cache = cache;
	}

	@Override
	public boolean canMap(Object object) {
		return object instanceof HashSet;
	}

	@Override
	public void map(Object object, ObjectOutputSerializer output) throws IOException {
		HashSet<?> set = (HashSet<?>) object;
		cache.writeFromDescription(object, output);
		HashMap<?, ?> map = getMap(set);
		try(StreamBuffer buffer = output.getBlockDataBuffer()) {
			buffer.writeInt(HashMapMapper.getCapacity(map));
			buffer.writeFloat(HashMapMapper.getLoadFactor(map));
			buffer.writeInt(map.size());
		}
		for(Object e : map.keySet()) {
			output.writeObject(e);
		}
		output.writeByte(ObjectStreamConstants.TC_ENDBLOCKDATA);
	}

	private HashMap<?, ?> getMap(HashSet<?> set) {
		try {
			return (HashMap<?, ?>) MAP.get(set);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return new HashMap<>();
		}
	}
}
