package org.ee.serialization.serialization.serializable.mapper.standard;

import java.io.IOException;
import java.io.ObjectStreamConstants;
import java.util.ArrayList;

import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescription;
import org.ee.serialization.serialization.serializable.ObjectOutputStreamSerializer;
import org.ee.serialization.serialization.serializable.mapper.ClassMapper;
import org.ee.serialization.serialization.serializable.mapper.SerializableMapper;
import org.ee.serialization.serialization.serializable.output.SerializableDataOutputStream;
import org.ee.serialization.serialization.serializable.output.StreamBuffer;

public class ArrayListMapper implements SerializableMapper {
	private final ClassMapper mapper;

	public ArrayListMapper(ClassMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public boolean canMap(Object object) {
		return object instanceof ArrayList;
	}

	@Override
	public void map(Object object, SerializableDataOutputStream output) throws IOException {
		ArrayList<?> list = (ArrayList<?>) object;
		output.writeByte(ObjectStreamConstants.TC_OBJECT);
		ClassDescription description = mapper.getClassDescription(object.getClass());
		output.writeObject(description);
		boolean cont = true;
		while(description != null && cont) {
			cont = mapper.writeDescription(object, output, description);
			description = description.getInfo().getSuperClass();
		}
		try(StreamBuffer buffer = output.getStreamBuffer()) {
			//TODO
			ObjectOutputStreamSerializer.writeBlockDataHeader(output, buffer.size());
		}
		output.writeByte(ObjectStreamConstants.TC_ENDBLOCKDATA);
	}
}
