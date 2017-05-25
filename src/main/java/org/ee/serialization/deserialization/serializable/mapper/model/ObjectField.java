package org.ee.serialization.deserialization.serializable.mapper.model;

import java.io.IOException;
import java.io.ObjectOutput;

public class ObjectField extends Field {
	private final String className;

	public ObjectField(ClassDescription description, char typeCode, String name, String className) {
		super(description, typeCode, name);
		this.className = className;
	}

	public ObjectField(ClassDescription description, char typeCode, String name, String className, java.lang.reflect.Field field) {
		super(description, typeCode, name, field);
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public static boolean isObject(char typeCode) {
		return typeCode == '[' || typeCode == 'L';
	}

	@Override
	public void writeTo(ObjectOutput output) throws IOException {
		super.writeTo(output);
		output.writeObject(className);
	}

	@Override
	public String toString() {
		return className + " " + getName();
	}
}
