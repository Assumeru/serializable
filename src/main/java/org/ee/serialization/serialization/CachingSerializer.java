package org.ee.serialization.serialization;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ee.serialization.Serializer;

public abstract class CachingSerializer implements Serializer {
	private final Map<Object, Reference> references;

	public CachingSerializer() {
		references = new HashMap<>();
	}

	@Override
	public void writeObject(Object object) throws IOException {
		if(shouldCache(object)) {
			Reference ref = references.get(object);
			if(ref != null) {
				object = ref;
			} else {
				references.put(object, new Reference(references.size()));
			}
		}
		writeObjectOrReference(object);
	}

	protected abstract void writeObjectOrReference(Object object) throws IOException;

	protected boolean shouldCache(Object object) {
		return object != null && object.getClass() != Boolean.class && object.getClass() != String.class && !(object instanceof Number);
	}
}
