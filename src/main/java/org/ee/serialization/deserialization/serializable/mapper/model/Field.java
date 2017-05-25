package org.ee.serialization.deserialization.serializable.mapper.model;

import java.io.IOException;
import java.io.ObjectOutput;

public abstract class Field implements ObjectOutputWriteable {
	private static final Field[] NONE = {};
	private final ClassDescription description;
	private final char typeCode;
	private final String name;
	private java.lang.reflect.Field field;

	public static Field[] createFields(int size) {
		if(size == 0) {
			return NONE;
		}
		return new Field[size];
	}

	public Field(ClassDescription description, char typeCode, String name, java.lang.reflect.Field field) {
		this(description, typeCode, name);
		this.field = field;
	}

	public Field(ClassDescription description, char typeCode, String name) {
		this.description = description;
		this.typeCode = typeCode;
		this.name = name;
	}

	public char getTypeCode() {
		return typeCode;
	}

	public String getName() {
		return name;
	}

	public ClassDescription getDescription() {
		return description;
	}

	public java.lang.reflect.Field getField() throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		if(field == null) {
			field = description.getType().getDeclaredField(name);
			field.setAccessible(true);
		}
		return field;
	}

	@Override
	public void writeTo(ObjectOutput output) throws IOException {
		output.writeByte(typeCode);
		output.writeUTF(name);
	}

	@Override
	public String toString() {
		return typeCode + " " + name;
	}

	public static char getTypeCode(Class<?> type) {
		if(type.isPrimitive()) {
			if(type == boolean.class) {
				return 'Z';
			} else if(type == byte.class) {
				return 'B';
			} else if(type == char.class) {
				return 'C';
			} else if(type == double.class) {
				return 'D';
			} else if(type == float.class) {
				return 'F';
			} else if(type == int.class) {
				return 'I';
			} else if(type == long.class) {
				return 'J';
			} else if(type == short.class) {
				return 'S';
			} else {
				throw new IllegalArgumentException(type + " is not a valid field type");
			}
		} else if(type.isArray()) {
			return '[';
		}
		return 'L';
	}
}
