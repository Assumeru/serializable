package org.ee.serialization.serialization.serializable.mapper;

import java.io.IOException;
import java.io.ObjectStreamConstants;

import org.ee.serialization.serialization.Reference;
import org.ee.serialization.serialization.serializable.output.SerializableDataOutputStream;

public class ReferenceMapper implements SerializableMapper {
	@Override
	public boolean canMap(Object object) {
		return object instanceof Reference;
	}

	@Override
	public void map(Object object, SerializableDataOutputStream output) throws IOException {
		Reference ref = (Reference) object;
		output.writeByte(ObjectStreamConstants.TC_REFERENCE);
		output.writeInt(ref.getIndex() + ObjectStreamConstants.baseWireHandle);
	}
}
