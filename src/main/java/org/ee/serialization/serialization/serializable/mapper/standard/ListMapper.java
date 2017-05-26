package org.ee.serialization.serialization.serializable.mapper.standard;

import java.io.IOException;
import java.io.ObjectStreamConstants;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescription;
import org.ee.serialization.serialization.serializable.ObjectOutputStreamSerializer;
import org.ee.serialization.serialization.serializable.mapper.ClassDescriptionManager;
import org.ee.serialization.serialization.serializable.mapper.SerializableMapper;
import org.ee.serialization.serialization.serializable.output.ObjectOutputSerializer;

public class ListMapper implements SerializableMapper {
	private final ClassDescriptionManager cache;

	public ListMapper(ClassDescriptionManager cache) {
		this.cache = cache;
	}

	@Override
	public boolean canMap(Object object) {
		return object instanceof ArrayList || object instanceof LinkedList;
	}

	@Override
	public void map(Object object, ObjectOutputSerializer output) throws IOException {
		List<?> list = (List<?>) object;
		output.writeByte(ObjectStreamConstants.TC_OBJECT);
		ClassDescription description = cache.getClassDescription(object.getClass());
		output.writeObject(description);
		output.assignHandle(object);
		boolean cont = true;
		while(description != null && cont) {
			cont = cache.writeFromDescription(object, output, description);
			description = description.getInfo().getSuperClass();
		}
		ObjectOutputStreamSerializer.writeBlockDataHeader(output, Integer.SIZE / Byte.SIZE);
		output.writeInt(list.size());
		for(Object value : list) {
			output.writeObject(value);
		}
		output.writeByte(ObjectStreamConstants.TC_ENDBLOCKDATA);
	}
}
