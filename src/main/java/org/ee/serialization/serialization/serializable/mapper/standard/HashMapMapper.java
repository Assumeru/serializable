package org.ee.serialization.serialization.serializable.mapper.standard;

import java.io.IOException;
import java.io.ObjectStreamConstants;
import java.util.HashMap;
import java.util.Map;

import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescription;
import org.ee.serialization.serialization.serializable.ObjectOutputStreamSerializer;
import org.ee.serialization.serialization.serializable.mapper.ClassMapper;
import org.ee.serialization.serialization.serializable.mapper.SerializableMapper;
import org.ee.serialization.serialization.serializable.output.ObjectOutputSerializer;
import org.ee.serialization.serialization.serializable.output.StreamBuffer;

public class HashMapMapper implements SerializableMapper {
	private final ClassMapper mapper;

	public HashMapMapper(ClassMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public boolean canMap(Object object) {
		return object instanceof HashMap;
	}

	@Override
	public void map(Object object, ObjectOutputSerializer output) throws IOException {
		HashMap<?, ?> map = (HashMap<?, ?>) object;
		output.writeByte(ObjectStreamConstants.TC_OBJECT);
		ClassDescription description = mapper.getClassDescription(object.getClass());
		output.writeObject(description);
		boolean cont = true;
		while(description != null && cont) {
			cont = mapper.writeDescription(object, output, description);
			description = description.getInfo().getSuperClass();
		}
		try(StreamBuffer buffer = output.getStreamBuffer()) {
			buffer.writeInt(map.size());
			buffer.writeInt(map.size());
			for(Map.Entry<?, ?> entry : map.entrySet()) {
				buffer.writeObject(entry.getKey());
				buffer.writeObject(entry.getValue());
			}
			ObjectOutputStreamSerializer.writeBlockDataHeader(output, buffer.size());
		}
		output.writeByte(ObjectStreamConstants.TC_ENDBLOCKDATA);
	}
}
