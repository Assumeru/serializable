package org.ee.serialization.serialization.serializable.mapper;

import java.io.IOException;
import java.io.ObjectStreamConstants;

import org.ee.serialization.serialization.serializable.output.ObjectOutputSerializer;

public class PrimitiveMapper implements SerializableMapper {
	@Override
	public void map(Object object, ObjectOutputSerializer output) throws IOException {
		if(object == null) {
			output.writeByte(ObjectStreamConstants.TC_NULL);
		} else if(object.getClass() == String.class) {
			String string = (String) object;
			if(string.length() > 0xFFFF) {
				output.write(ObjectStreamConstants.TC_LONGSTRING);
				//TODO potentially incorrect
				output.write(string.getBytes("utf-8"));
			} else {
				output.writeByte(ObjectStreamConstants.TC_STRING);
				output.writeUTF(string);
			}
		} else if(object.getClass() == Boolean.class) {
			output.writeBoolean((Boolean) object);
		} else if(object.getClass() == Character.class) {
			output.writeChar((Character) object);
		} else if(object.getClass() == Byte.class) {
			output.writeByte((Byte) object);
		} else if(object.getClass() == Double.class) {
			output.writeDouble((Double) object);
		} else if(object.getClass() == Float.class) {
			output.writeFloat((Float) object);
		} else if(object.getClass() == Integer.class) {
			output.writeInt((Integer) object);
		} else if(object.getClass() == Long.class) {
			output.writeLong((Long) object);
		} else if(object.getClass() == Short.class) {
			output.writeShort((Short) object);
		}
	}

	@Override
	public boolean canMap(Object object) {
		return object == null || object.getClass() == String.class || object.getClass() == Boolean.class
				|| object.getClass() == Character.class || object instanceof Number;
	}
}
