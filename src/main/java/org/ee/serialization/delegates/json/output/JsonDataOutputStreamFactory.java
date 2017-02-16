package org.ee.serialization.delegates.json.output;

import java.io.IOException;
import java.io.OutputStream;

import org.ee.serialization.Config;

public interface JsonDataOutputStreamFactory {
	JsonDataOutputStream createJsonDataOutputStream(OutputStream output, Config config) throws IOException;
}
