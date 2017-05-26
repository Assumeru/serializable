package org.ee.serialization.serialization.serializable.mapper;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamConstants;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ee.serialization.SerializationException;
import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescription;
import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescriptionInfo;
import org.ee.serialization.deserialization.serializable.mapper.model.Field;
import org.ee.serialization.deserialization.serializable.mapper.model.ObjectField;
import org.ee.serialization.deserialization.serializable.mapper.model.PrimitiveField;
import org.ee.serialization.serialization.serializable.ObjectOutputStreamSerializer;
import org.ee.serialization.serialization.serializable.output.ObjectOutputSerializer;
import org.ee.serialization.serialization.serializable.output.StreamBuffer;

public class ClassDescriptionManager {
	private static final Method WRITE_EXTERNAL;
	static {
		Method writeExternal;
		try {
			writeExternal = Externalizable.class.getMethod("writeExternal", ObjectOutput.class);
		} catch (NoSuchMethodException | SecurityException e) {
			writeExternal = null;
		}
		WRITE_EXTERNAL = writeExternal;
	}
	private final Map<Class<?>, ClassDescription> classDescriptions;

	public ClassDescriptionManager() {
		classDescriptions = new HashMap<>();
	}

	public ClassDescription getClassDescription(Class<?> type) throws SerializationException {
		ClassDescription description = classDescriptions.get(type);
		if(description == null) {
			if(Proxy.isProxyClass(type)) {
				//TODO proxy classes
				throw new SerializationException("No support for proxy classes: " + type);
			}
			description = new ClassDescription(type.getName(), getVersionId(type));
			classDescriptions.put(type, description);
			byte flags = getFlags(type);
			Field[] fields = getFields(type, description);
			Class<?> superClass = type.getSuperclass();
			ClassDescriptionInfo info = new ClassDescriptionInfo(flags, fields, Collections.emptyList(), superClass != null && Serializable.class.isAssignableFrom(superClass) ? getClassDescription(superClass) : null);
			description.setInfo(info);
		}
		return description;
	}

	private byte getFlags(Class<?> type) throws SerializationException {
		byte flags = 0;
		try {
			Method method = type.getDeclaredMethod("writeObject", ObjectOutputStream.class);
			int modifiers = method.getModifiers();
			if(method.getReturnType() == Void.TYPE && !Modifier.isStatic(modifiers) && Modifier.isPrivate(modifiers)) {
				flags |= ObjectStreamConstants.SC_WRITE_METHOD;
			}
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
			throw new SerializationException(e);
		}
		if(Externalizable.class.isAssignableFrom(type)) {
			flags |= ObjectStreamConstants.SC_EXTERNALIZABLE;
			flags |= ObjectStreamConstants.SC_BLOCK_DATA;
		} else if(Serializable.class.isAssignableFrom(type)) {
			flags |= ObjectStreamConstants.SC_SERIALIZABLE;
		}
		if(Enum.class.isAssignableFrom(type)) {
			flags |= ObjectStreamConstants.SC_ENUM;
		}
		return flags;
	}

	private Field[] getFields(Class<?> type, ClassDescription description) {
		if(Enum.class.isAssignableFrom(type) || Externalizable.class.isAssignableFrom(type)) {
			return Field.createFields(0);
		}
		List<Field> fields = new ArrayList<>();
		for(java.lang.reflect.Field field : type.getDeclaredFields()) {
			int modifiers = field.getModifiers();
			if(!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
				Field f;
				Class<?> fieldType = field.getType();
				if(fieldType.isPrimitive()) {
					f = new PrimitiveField(description, Field.getTypeCode(fieldType), field.getName(), field);
				} else {
					String fieldDescriptor;
					if(fieldType.isArray()) {
						fieldDescriptor = fieldType.getName();
					} else {
						fieldDescriptor = new StringBuilder().append('L').append(fieldType.getName().replace('.', '/')).append(';').toString();
					}
					f = new ObjectField(description, Field.getTypeCode(fieldType), field.getName(), fieldDescriptor, field);
				}
				fields.add(f);
			}
		}
		Collections.sort(fields);
		return fields.toArray(Field.createFields(fields.size()));
	}

	private long getVersionId(Class<?> type) throws SerializationException {
		if(Enum.class.isAssignableFrom(type)) {
			return 0;
		}
		try {
			java.lang.reflect.Field field = type.getDeclaredField("serialVersionUID");
			field.setAccessible(true);
			return field.getLong(null);
		} catch (NoSuchFieldException | NullPointerException e) {
		} catch (SecurityException | IllegalAccessException e) {
			throw new SerializationException(e);
		}
		return 0;
	}

	public boolean writeFromDescription(Object object, ObjectOutputSerializer output, ClassDescription description) throws IOException {
		if(description.getInfo().hasFlag(ObjectStreamConstants.SC_EXTERNALIZABLE)) {
			if(WRITE_EXTERNAL == null) {
				throw new SerializationException("Could not access Externalizable.writeExternal");
			}
			try(StreamBuffer buffer = output.getStreamBuffer()) {
				WRITE_EXTERNAL.invoke(object, buffer);
				ObjectOutputStreamSerializer.writeBlockDataHeader(output, buffer.size());
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new SerializationException(e);
			}
			output.writeByte(ObjectStreamConstants.TC_ENDBLOCKDATA);
			return false;
		}
		try {
			description.getInfo().writeFieldValues(output, object);
		} catch (NoSuchFieldException | SecurityException | ClassNotFoundException | IllegalAccessException e) {
			throw new SerializationException(e);
		}
		return true;
	}
}
