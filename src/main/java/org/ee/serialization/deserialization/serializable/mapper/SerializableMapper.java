package org.ee.serialization.deserialization.serializable.mapper;

import java.io.IOException;
import java.io.ObjectStreamConstants;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.WeakHashMap;

import org.ee.serialization.SerializationException;
import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescription;
import org.ee.serialization.deserialization.serializable.mapper.model.FieldValue;
import org.ee.serialization.deserialization.serializable.mapper.model.ObjectMapping;
import org.ee.serialization.deserialization.serializable.mapper.platform.AndroidConstructor;
import org.ee.serialization.deserialization.serializable.mapper.platform.JreConstructor;
import org.ee.serialization.deserialization.serializable.mapper.platform.NativeConstructor;
import org.ee.serialization.deserialization.serializable.mapper.platform.ReflectionConstuctor;

public class SerializableMapper implements ObjectInputStreamMapperDelegate {
	private static final int STATIC_FINAL = Modifier.STATIC | Modifier.FINAL;
	private static final NativeConstructor NATIVE_CONSTRUCTOR;
	static {
		NativeConstructor constructor;
		try {
			constructor = new JreConstructor();
		} catch(UnsupportedOperationException e) {
			try {
				constructor = new AndroidConstructor();
			} catch(UnsupportedOperationException e2) {
				constructor = new ReflectionConstuctor();
			}
		}
		NATIVE_CONSTRUCTOR = constructor;
	}
	private final Map<ClassDescription, Boolean> mappable;

	public SerializableMapper() {
		this.mappable = new WeakHashMap<>();
	}

	@Override
	public Object map(Object object, ObjectInputStreamMapper mapper) throws IOException, ClassNotFoundException {
		if(object instanceof ObjectMapping) {
			ObjectMapping mapping = (ObjectMapping) object;
			try {
				Object out = newInstance(mapping.getDescription(), mappable);
				for(FieldValue value : mapping.getFields()) {
					Class<?> fieldClass = Class.forName(value.getField().getDescription().getName());
					Field field = fieldClass.getDeclaredField(value.getField().getName());
					field.setAccessible(true);
					field.set(out, mapper.map(value.getValue(), mapper));
				}
				return out;
			} catch(ClassNotFoundException e) {
				throw e;
			} catch (ReflectiveOperationException | SecurityException e) {
				throw new SerializationException("Failed to deserialize", e);
			}
		}
		return object;
	}

	static Object newInstance(ClassDescription description, Map<ClassDescription, Boolean> mappable) throws ReflectiveOperationException, SerializationException {
		try {
			return NATIVE_CONSTRUCTOR.newInstance(description);
		} catch(SerializationException | ReflectiveOperationException | IllegalArgumentException e) {
			mappable.put(description, false);
			throw e;
		}
	}

	@Override
	public boolean canMap(Object object) {
		if(object instanceof ObjectMapping) {
			ClassDescription description = ((ObjectMapping) object).getDescription();
			Boolean canBeMapped = mappable.get(description);
			if(canBeMapped != null) {
				return canBeMapped;
			}
			if(description.getInfo().getFlags() == ObjectStreamConstants.SC_SERIALIZABLE) {
				canBeMapped = classExists(description);
			} else {
				canBeMapped = false;
			}
			mappable.put(description, canBeMapped);
			return canBeMapped;
		}
		return false;
	}

	static boolean classExists(ClassDescription description) {
		try {
			Class<?> type = Class.forName(description.getName());
			if(type.isArray()) {
				return true;
			}
			Long suid = getSerialVersionUID(type);
			if(suid != null) {
				return description.getSerialVersionUID() == suid;
			}
		} catch (Exception e) {
		}
		return false;
	}

	private static Long getSerialVersionUID(Class<?> type) throws NoSuchFieldException, IllegalAccessException {
		Field field = type.getDeclaredField("serialVersionUID");
		if((field.getModifiers() & STATIC_FINAL) == STATIC_FINAL) {
            field.setAccessible(true);
            return field.getLong(null);
		}
		return null;
	}
}
