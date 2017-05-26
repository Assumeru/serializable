package org.ee.serialization.deserialization.serializable.mapper.model;

import java.io.IOException;
import java.io.ObjectStreamConstants;

public class ClassMapping implements ObjectOutputWriteable {
	private final ClassDescription description;

	public ClassMapping(ClassDescription description) {
		this.description = description;
	}

	public ClassDescription getDescription() {
		return description;
	}

	@Override
	public void writeTo(CachingObjectOutput output) throws IOException {
		output.writeByte(ObjectStreamConstants.TC_CLASS);
		output.writeObject(description);
		output.assignHandle(this);
	}
}
