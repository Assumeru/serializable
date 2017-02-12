package org.ee.serialization.delegates.serializable.mapper.model;

public class EnumMapping extends ObjectMapping {
	private final String name;

	public EnumMapping(ClassDescription description, String name) {
		super(description);
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
