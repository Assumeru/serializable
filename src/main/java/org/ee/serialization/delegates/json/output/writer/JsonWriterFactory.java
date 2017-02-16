package org.ee.serialization.delegates.json.output.writer;

import java.io.OutputStream;

import org.ee.serialization.Config;

public interface JsonWriterFactory {
	JsonWriter createWriter(OutputStream output, Config config);
}
