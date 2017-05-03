package org.ee.serialization.deserialization.serializable;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamConstants;
import java.util.Objects;

import org.ee.serialization.Config;
import org.ee.serialization.Config.Key;
import org.ee.serialization.deserialization.DelegateFactory;
import org.ee.serialization.deserialization.DeserializationDelegate;
import org.ee.serialization.deserialization.serializable.mapper.ObjectInputStreamMapper;

public class ObjectInputStreamDelegateFactory implements DelegateFactory {
	public static final Key<Boolean> USE_NATIVE = new Key<>();
	public static final Key<ObjectInputStreamMapper> MAPPER = new Key<>();

	@Override
	public DeserializationDelegate createDeserializer(InputStream input, Config config) throws IOException {
		Boolean useNative = config.getFactorySetting(USE_NATIVE);
		if(Objects.equals(useNative, Boolean.FALSE)) {
			return new ObjectInputStreamMapperDelegate(input, config.getFactorySetting(MAPPER));
		}
		return new ObjectInputStreamDelegate(input);
	}

	@Override
	public boolean matches(short magicNumber, Config config) {
		return magicNumber == ObjectStreamConstants.STREAM_MAGIC;
	}
}
