package org.ee.serialization.serialization.json.mapper;

import java.io.IOException;

import org.ee.serialization.serialization.json.output.JsonDataOutputStream;

public interface JsonMapper {
	boolean canMap(Object object);

	void map(Object object, JsonDataOutputStream output) throws IOException;
}
