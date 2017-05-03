package org.ee.serialization.deserialization.serializable.mapper.platform;

import org.ee.serialization.SerializationException;
import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescription;

public interface NativeConstructor {
	Object newInstance(ClassDescription description) throws SerializationException, ReflectiveOperationException, IllegalArgumentException;
}
