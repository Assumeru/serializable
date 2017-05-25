package org.ee.serialization.serialization.serializable.mapper;

import java.io.IOException;
import java.io.ObjectStreamConstants;

import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescription;
import org.ee.serialization.deserialization.serializable.mapper.model.ObjectOutputWriteable;
import org.ee.serialization.serialization.serializable.output.SerializableDataOutputStream;

public class ObjectMapper implements SerializableMapper {
	private final ClassMapper mapper;

	public ObjectMapper(ClassMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public boolean canMap(Object object) {
		return true;
	}

	@Override
	public void map(Object object, SerializableDataOutputStream output) throws IOException {
		if(object instanceof ObjectOutputWriteable) {
			((ObjectOutputWriteable) object).writeTo(output);
		} else {
			output.writeByte(ObjectStreamConstants.TC_OBJECT);
			ClassDescription description = mapper.getClassDescription(object.getClass());
			output.writeObject(description);
			boolean cont = true;
			while(description != null && cont) {
				cont = mapper.writeDescription(object, output, description);
				description = description.getInfo().getSuperClass();
			}
		}
	}
}
