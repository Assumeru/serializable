package org.ee.serialization.deserialization.serializable.mapper.model;

import java.io.IOException;
import java.io.ObjectOutput;
import java.util.Objects;

public class FieldValue implements ObjectOutputWriteable {
	private final Field field;
	private final Object value;

	static void writeType(ObjectOutput output, char type, Object value) throws IOException {
		switch(type) {
		case 'B':
			output.writeByte((Byte) value);
			break;
		case 'C':
			output.writeChar((Character) value);
			break;
		case 'D':
			output.writeDouble((Double) value);
			break;
		case 'F':
			output.writeFloat((Float) value);
			break;
		case 'I':
			output.writeInt((Integer) value);
			break;
		case 'J':
			output.writeLong((Long) value);
			break;
		case 'S':
			output.writeShort((Short) value);
			break;
		case 'Z':
			output.writeBoolean((Boolean) value);
			break;
		case 'L':
		case '[':
			output.writeObject(value);
			break;
		}
	}

	public FieldValue(Field field, Object value) {
		this.field = field;
		this.value = value;
	}

	public Field getField() {
		return field;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return field + "=" + value;
	}

	@Override
	public void writeTo(CachingObjectOutput output) throws IOException {
		writeType(output, field.getTypeCode(), value);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		} else if(obj instanceof FieldValue) {
			FieldValue other = (FieldValue) obj;
			return field.equals(other.field) && Objects.deepEquals(value, other.value);
		}
		return false;
	}
}
