package org.ee.serialization.delegates.json;

import org.ee.serialization.delegates.json.output.JsonDataOutputStream;

public interface JsonSerializable {
	void toJson(JsonDataOutputStream output);
}
