package org.ee.serialization.deserialization.serializable.mapper.standard;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.HashSet;

import org.ee.serialization.SerializationException;
import org.ee.serialization.deserialization.serializable.mapper.ObjectInputStreamMapper;
import org.ee.serialization.deserialization.serializable.mapper.model.ObjectMapping;

@SuppressWarnings("rawtypes")
public class HashSetMapper extends AbstractMapper<HashSet> {
	public HashSetMapper() {
		super(HashSet.class);
	}

	@Override
	protected HashSet doMap(ObjectMapping object, ObjectInputStreamMapper mapper) throws IOException {
		ObjectInput input = object.getData(mapper);
		input.readInt();
		input.readFloat();
		int size = input.readInt();
		HashSet<Object> set = new HashSet<>();
		try {
			for(int i = 0; i < size; i++) {
				set.add(input.readObject());
			}
		} catch(ClassNotFoundException e) {
			throw new SerializationException("Failed to map HashSet", e);
		}
		return set;
	}
}
