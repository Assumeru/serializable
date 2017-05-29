package org.ee.serialization.deserialization.serializable.mapper.standard;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.LinkedList;

import org.ee.serialization.SerializationException;
import org.ee.serialization.deserialization.serializable.mapper.ObjectInputStreamMapper;
import org.ee.serialization.deserialization.serializable.mapper.model.ObjectMapping;

@SuppressWarnings("rawtypes")
public class LinkedListMapper extends AbstractMapper<LinkedList> {
	public LinkedListMapper() {
		super(LinkedList.class);
	}

	@Override
	protected LinkedList<?> doMap(ObjectMapping object, ObjectInputStreamMapper mapper) throws IOException {
		ObjectInput input = object.getData(mapper);
		int size = input.readInt();
		LinkedList<Object> out = new LinkedList<>();
		mapper.cache(object, out);
		try {
			while(size > 0) {
				out.add(input.readObject());
				size--;
			}
		} catch(ClassNotFoundException e) {
			throw new SerializationException("Failed to map LinkedList", e);
		}
		return out;
	}
}
