package org.ee.serialization.delegates.serializable.mapper.model;

public abstract class Field {
	private static final Field[] NONE = {};
	private final ClassDescription description;
	private final char typeCode;
	private final String name;

	public static Field[] createFields(int size) {
		if(size == 0) {
			return NONE;
		}
		return new Field[size];
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

	@Override
	public String toString() {
		return typeCode + " " + name;
	}
}
