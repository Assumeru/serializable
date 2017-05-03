package org.ee.serialization.deserialization;

import java.io.IOException;
import java.io.InputStream;

import org.ee.serialization.Config;

public interface DelegateFactory {
	DeserializationDelegate createDeserializer(InputStream input, Config config) throws IOException;

	boolean matches(short magicNumber, Config config);
}
