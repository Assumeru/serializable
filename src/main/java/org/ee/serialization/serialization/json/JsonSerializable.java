package org.ee.serialization.serialization.json;

import java.io.IOException;

import org.ee.serialization.serialization.json.output.JsonDataOutputStream;

public interface JsonSerializable {
	void toJson(JsonDataOutputStream output) throws IOException;
}
