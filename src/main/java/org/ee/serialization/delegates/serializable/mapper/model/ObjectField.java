package org.ee.serialization.delegates.serializable.mapper.model;

public class ObjectField extends Field {
	private final String className;

	public ObjectField(ClassDescription description, char typeCode, String name, String className) {
		super(description, typeCode, name);
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public static boolean isObject(char typeCode) {
		return typeCode == '[' || typeCode == 'L';
	}

	@Override
	public String toString() {
		return className + " " + getName();
	}
}
