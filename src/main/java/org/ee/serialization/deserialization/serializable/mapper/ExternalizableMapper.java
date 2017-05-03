package org.ee.serialization.deserialization.serializable.mapper;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectStreamConstants;
import java.util.Map;
import java.util.WeakHashMap;

import org.ee.serialization.SerializationException;
import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescription;
import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescriptionInfo;
import org.ee.serialization.deserialization.serializable.mapper.model.ObjectMapping;

public class ExternalizableMapper implements ObjectInputStreamMapperDelegate {
	private final Map<ClassDescription, Boolean> mappable;

	public ExternalizableMapper() {
		this.mappable = new WeakHashMap<>();
	}

	@Override
	public Object map(Object object, ObjectInputStreamMapper mapper) throws IOException, ClassNotFoundException {
		if(object instanceof ObjectMapping) {
			ObjectMapping mapping = (ObjectMapping) object;
			try {
				Externalizable out = (Externalizable) SerializableMapper.newInstance(mapping.getDescription(), mappable);
				out.readExternal(mapping.getData(mapper));
				return out;
			} catch (ReflectiveOperationException e) {
				throw new SerializationException("Failed to deserialize", e);
			}
		}
		return object;
	}

	@Override
	public boolean canMap(Object object) {
		if(object instanceof ObjectMapping) {
			ClassDescription description = ((ObjectMapping) object).getDescription();
			Boolean canBeMapped = mappable.get(description);
			if(canBeMapped != null) {
				return canBeMapped;
			}
			ClassDescriptionInfo info = description.getInfo();
			if(info.hasFlag(ObjectStreamConstants.SC_EXTERNALIZABLE) && info.hasFlag(ObjectStreamConstants.SC_BLOCK_DATA)) {
				canBeMapped = SerializableMapper.classExists(description);
			} else {
				canBeMapped = false;
			}
			mappable.put(description, canBeMapped);
			return canBeMapped;
		}
		return false;
	}
}
