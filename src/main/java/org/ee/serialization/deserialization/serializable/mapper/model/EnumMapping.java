package org.ee.serialization.deserialization.serializable.mapper.model;

import java.io.IOException;

import org.ee.serialization.serialization.json.output.JsonDataOutputStream;

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
}
