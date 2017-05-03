package org.ee.serialization.deserialization.serializable.mapper.standard;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.HashMap;

import org.ee.serialization.SerializationException;
import org.ee.serialization.deserialization.serializable.mapper.ObjectInputStreamMapper;
import org.ee.serialization.deserialization.serializable.mapper.model.ObjectMapping;

@SuppressWarnings("rawtypes")
public class HashMapMapper extends AbstractMapper<HashMap> {
	public HashMapMapper() {
		super(HashMap.class);
	}

	@Override
	protected HashMap<?, ?> doMap(ObjectMapping object, ObjectInputStreamMapper mapper) throws IOException {
		ObjectInput input = object.getData(mapper);
		int capacity = input.readInt();
		HashMap<Object, Object> map = new HashMap<>(capacity);
		int size = input.readInt();
		try {
			while(size > 0) {
				Object key = input.readObject();
				Object value = input.readObject();
				map.put(key, value);
				size--;
			}
		} catch(ClassNotFoundException e) {
			throw new SerializationException("Failed to map HashMap", e);
		}
		return map;
	}
}
