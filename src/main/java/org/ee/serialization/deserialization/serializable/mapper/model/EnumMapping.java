package org.ee.serialization.deserialization.serializable.mapper.model;

import java.io.IOException;
import java.io.ObjectStreamConstants;

import org.ee.serialization.serialization.json.output.JsonDataOutputStream;
import org.ee.serialization.serialization.serializable.output.CachingObjectOutput;

public class EnumMapping extends ObjectMapping {
	private final String name;

	public EnumMapping(ClassDescription description, String name) {
		super(description);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public void toJson(JsonDataOutputStream output) throws IOException {
		output.beginObject();
		ClassDescription description = getDescription();
		output.name(JsonDataOutputStream.PROPERTY_CLASS).value(description.getName());
		output.name(JsonDataOutputStream.PROPERTY_VERSION).value(description.getSerialVersionUID());
		output.name(JsonDataOutputStream.PROPERTY_ENUM_VALUE).value(name);
		output.endObject();
	}

	@Override
	public void writeTo(CachingObjectOutput output) throws IOException {
		output.writeByte(ObjectStreamConstants.TC_ENUM);
		output.writeObject(getDescription());
		output.assignHandle(this);
		output.writeObject(name);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		} else if(obj instanceof EnumMapping) {
			EnumMapping other = (EnumMapping) obj;
			return getDescription().equals(other.getDescription()) && name.equals(other.name);
		}
		return false;
	}
}
