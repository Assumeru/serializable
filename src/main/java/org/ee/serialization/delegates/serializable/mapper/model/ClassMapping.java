package org.ee.serialization.delegates.serializable.mapper.model;

public class ClassMapping {
	private final ClassDescription description;

	public ClassMapping(ClassDescription description) {
		this.description = description;
	}

	public ClassDescription getDescription() {
		return description;
	}
}
