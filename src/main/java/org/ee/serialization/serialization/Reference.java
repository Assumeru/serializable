package org.ee.serialization.serialization;

import java.io.IOException;

import org.ee.serialization.serialization.json.JsonSerializable;
import org.ee.serialization.serialization.json.output.JsonDataOutputStream;

public class Reference implements JsonSerializable {
	private final int index;

	public Reference(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public void toJson(JsonDataOutputStream output) throws IOException {
		output.beginObject();
		output.name(JsonDataOutputStream.PROPERTY_CLASS).value(Reference.class.getName());
		output.name("index").value(index);
		output.endObject();
	}
}
