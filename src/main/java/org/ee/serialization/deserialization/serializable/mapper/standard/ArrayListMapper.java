package org.ee.serialization.deserialization.serializable.mapper.standard;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.ArrayList;

import org.ee.serialization.SerializationException;
import org.ee.serialization.deserialization.serializable.mapper.ObjectInputStreamMapper;
import org.ee.serialization.deserialization.serializable.mapper.model.ObjectMapping;

@SuppressWarnings("rawtypes")
public class ArrayListMapper extends AbstractMapper<ArrayList> {
	public ArrayListMapper() {
		super(ArrayList.class);
	}

	@Override
	protected ArrayList<?> doMap(ObjectMapping object, ObjectInputStreamMapper mapper) throws IOException {
		ObjectInput input = object.getData(mapper);
		int size = (Integer) object.getFields().get(0).getValue();
		ArrayList<Object> out = new ArrayList<>(Math.max(size, input.readInt()));
		mapper.cache(object, out);
		try {
			while(size > 0) {
				out.add(input.readObject());
				size--;
			}
		} catch(ClassNotFoundException e) {
			throw new SerializationException("Failed to map ArrayList", e);
		}
		return out;
	}
}
