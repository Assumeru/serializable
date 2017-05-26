package org.ee.serialization.deserialization.serializable.mapper;

import java.io.IOException;
import java.util.Map;
import java.util.WeakHashMap;

import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescription;
import org.ee.serialization.deserialization.serializable.mapper.model.ClassMapping;

public class ClassMapper implements ObjectInputStreamMapperDelegate {
	private final Map<ClassDescription, Boolean> mappable;

	public ClassMapper() {
		mappable = new WeakHashMap<>();
	}

	@Override
	public Object map(Object object, ObjectInputStreamMapper mapper) throws IOException, ClassNotFoundException {
		if(object instanceof ClassMapping) {
			ClassMapping mapping = (ClassMapping) object;
			return mapping.getDescription().getType();
		}
		return object;
	}

	@Override
	public boolean canMap(Object object) {
		if(object instanceof ClassMapping) {
			ClassDescription description = ((ClassMapping) object).getDescription();
			Boolean canBeMapped = mappable.get(description);
			if(canBeMapped != null) {
				return canBeMapped;
			}
			try {
				canBeMapped = description.getType() != null;
			} catch (ClassNotFoundException e) {
				canBeMapped = false;
			}
			mappable.put(description, canBeMapped);
			return canBeMapped;
		}
		return false;
	}
}
