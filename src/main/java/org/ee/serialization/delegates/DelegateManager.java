package org.ee.serialization.delegates;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.ee.serialization.Config;
import org.ee.serialization.SerializationException;
import org.ee.serialization.delegates.serializable.ObjectInputStreamDelegateFactory;

public class DelegateManager implements DelegateFactory {
	private static volatile DelegateManager instance;
	private final Collection<DelegateFactory> factories;

	protected DelegateManager() {
		this(new ArrayList<DelegateFactory>());
	}

	protected DelegateManager(Collection<DelegateFactory> factories) {
		this.factories = factories;
	}

	@Override
	public DeserializationDelegate createDeserializer(InputStream input, Config config) throws IOException {
		if(!input.markSupported()) {
			input = new BufferedInputStream(input);
		}
		input.mark(2);
		short magicNumber = readShort(input);
		input.reset();
		return findDelegate(magicNumber, input, config);
	}

	public short readShort(InputStream input) throws IOException {
		byte[] buffer = new byte[2];
		if(input.read(buffer) == 2) {
			return (short) ((buffer[1] & 0xFF) + (buffer[0] << 8));
		}
		throw new SerializationException("Failed to read short");
	}

	protected DeserializationDelegate findDelegate(short magicNumber, InputStream input, Config config) throws IOException {
		for(DelegateFactory factory : factories) {
			if(factory.matches(magicNumber, config)) {
				return factory.createDeserializer(input, config);
			}
		}
		throw new SerializationException(String.format("No suitable deserializer found for %04X", magicNumber));
	}

	@Override
	public boolean matches(short magicNumber, Config config) {
		return false;
	}

	public void registerFactory(DelegateFactory factory) {
		factories.add(factory);
	}

	public static DelegateManager getInstance() {
		if(instance == null) {
			synchronized(DelegateManager.class) {
				if(instance == null) {
					instance = new DelegateManager();
					instance.registerDefaults();
				}
			}
		}
		return instance;
	}

	protected void registerDefaults() {
		//TODO add more
		registerFactory(new ObjectInputStreamDelegateFactory());
	}
}
