package org.ee.serialization.deserialization.serializable.mapper.model;

public class PrimitiveField extends Field {
	public PrimitiveField(ClassDescription description, char typeCode, String name) {
		super(description, typeCode, name);
	}

	public PrimitiveField(ClassDescription description, char typeCode, String name, java.lang.reflect.Field field) {
		super(description, typeCode, name, field);
	}

	public static boolean isPrimitive(char typeCode) {
		switch(typeCode) {
		case 'B':
		case 'C':
		case 'D':
		case 'F':
		case 'I':
		case 'J':
		case 'S':
		case 'Z':
			return true;
		default:
			return false;
		}
	}
}
