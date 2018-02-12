package org.ee.serialization.serialization;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ee.serialization.Config.Key;
import org.ee.serialization.Serializer;

public abstract class CachingSerializer implements Serializer {
	public static final Key<Boolean> USE_IDENTITY_COMPARE = new Key<>();
	private final Map<Object, Reference> references;
	private final boolean addAutomatically;

	public CachingSerializer() {
		this(true, false);
	}

	public CachingSerializer(boolean addAutomatically, boolean identity) {
		if(identity) {
			references = new Cache();
		} else {
			references = new HashMap<>();
		}
		this.addAutomatically = addAutomatically;
	}

	@Override
	public final void writeObject(Object object) throws IOException {
		writeObjectOrReference(getFromCache(object));
	}

	protected final Object getFromCache(Object object) {
		Reference ref = references.get(object);
		if(ref != null) {
			return ref;
		} else if(addAutomatically && shouldCache(object)) {
			addToCache(object);
		}
		return object;
	}

	protected final void addToCache(Object object) {
		if(!references.containsKey(object)) {
			references.put(object, new Reference(references.size()));
		}
	}

	protected abstract void writeObjectOrReference(Object object) throws IOException;

	protected boolean shouldCache(Object object) {
		return object != null && object.getClass() != Boolean.class && object.getClass() != String.class && !(object instanceof Number);
	}
}
