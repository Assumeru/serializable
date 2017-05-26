package org.ee.serialization.serialization.serializable.mapper.standard;

import java.io.IOException;
import java.io.ObjectStreamConstants;
import java.util.Date;

import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescription;
import org.ee.serialization.serialization.serializable.mapper.ClassDescriptionManager;
import org.ee.serialization.serialization.serializable.mapper.SerializableMapper;
import org.ee.serialization.serialization.serializable.output.ObjectOutputSerializer;

public class DateMapper implements SerializableMapper {
	private final ClassDescriptionManager cache;

	public DateMapper(ClassDescriptionManager cache) {
		this.cache = cache;
	}

	@Override
	public boolean canMap(Object object) {
		return object instanceof Date;
	}

	@Override
	public void map(Object object, ObjectOutputSerializer output) throws IOException {
		Date date = (Date) object;
		output.writeByte(ObjectStreamConstants.TC_OBJECT);
		ClassDescription description = cache.getClassDescription(object.getClass());
		output.writeObject(description);
		output.assignHandle(object);
		output.writeByte(ObjectStreamConstants.TC_BLOCKDATA);
		output.writeByte(8);
		output.writeLong(date.getTime());
		output.writeByte(ObjectStreamConstants.TC_ENDBLOCKDATA);
	}
}
