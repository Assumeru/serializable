package org.ee.serialization.deserialization.serializable.mapper.model;

public class FieldValue {
	private final Field field;
	private final Object value;

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
}
