package org.ee.serialization.serialization.serializable.mapper.standard;

import java.io.IOException;
import java.io.ObjectStreamConstants;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescription;
import org.ee.serialization.serialization.serializable.ObjectOutputStreamSerializer;
import org.ee.serialization.serialization.serializable.mapper.ClassMapper;
import org.ee.serialization.serialization.serializable.mapper.SerializableMapper;
import org.ee.serialization.serialization.serializable.output.ObjectOutputSerializer;
import org.ee.serialization.serialization.serializable.output.StreamBuffer;

public class ListMapper implements SerializableMapper {
	private final ClassMapper mapper;

	public ListMapper(ClassMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public boolean canMap(Object object) {
		return object instanceof ArrayList || object instanceof LinkedList;
	}

	@Override
	public void map(Object object, ObjectOutputSerializer output) throws IOException {
		List<?> list = (List<?>) object;
		output.writeByte(ObjectStreamConstants.TC_OBJECT);
		ClassDescription description = mapper.getClassDescription(object.getClass());
		output.writeObject(description);
		boolean cont = true;
		while(description != null && cont) {
			cont = mapper.writeDescription(object, output, description);
			description = description.getInfo().getSuperClass();
		}
		try(StreamBuffer buffer = output.getStreamBuffer()) {
			buffer.writeInt(list.size());
			for(Object value : list) {
				buffer.writeObject(value);
			}
			ObjectOutputStreamSerializer.writeBlockDataHeader(output, buffer.size());
		}
		output.writeByte(ObjectStreamConstants.TC_ENDBLOCKDATA);
	}
}
