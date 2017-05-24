package org.ee.serialization.serialization.serializable.mapper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
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
import org.ee.serialization.deserialization.serializable.mapper.model.ObjectMapping;
import org.ee.serialization.deserialization.serializable.mapper.model.PrimitiveField;
import org.ee.serialization.serialization.serializable.output.SerializableDataOutputStream;

public class ObjectMapper implements SerializableMapper {
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

	public ObjectMapper() {
		classDescriptions = new HashMap<>();
	}

	@Override
	public boolean canMap(Object object) {
		return true;
	}

	@Override
	public void map(Object object, SerializableDataOutputStream output) throws IOException {
		if(object instanceof ClassDescription) {
			//TODO
		} else if(object instanceof ObjectMapping) {
			//TODO
		} else {
			output.writeByte(ObjectStreamConstants.TC_OBJECT);
			ClassDescription description = getClassDescription(object.getClass());
			output.writeObject(description);
			while(description != null) {
				//TODO break on externalizable?
				writeDescription(object, output, description);
				description = description.getInfo().getSuperClass();
			}
		}
	}

	private ClassDescription getClassDescription(Class<?> type) throws SerializationException {
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
			ClassDescriptionInfo info = new ClassDescriptionInfo(flags, fields, Collections.emptyList(), superClass == null ? null : getClassDescription(superClass));
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
		if(Serializable.class.isAssignableFrom(type)) {
			flags |= ObjectStreamConstants.SC_SERIALIZABLE;
		}
		if(Externalizable.class.isAssignableFrom(type)) {
			flags |= ObjectStreamConstants.SC_EXTERNALIZABLE;
		}
		flags |= ObjectStreamConstants.SC_BLOCK_DATA;
		if(Enum.class.isAssignableFrom(type)) {
			flags |= ObjectStreamConstants.SC_ENUM;
		}
		return flags;
	}

	private Field[] getFields(Class<?> type, ClassDescription description) {
		List<Field> fields = new ArrayList<>();
		for(java.lang.reflect.Field field : type.getDeclaredFields()) {
			int modifiers = field.getModifiers();
			if(!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
				Field f;
				Class<?> fieldType = field.getType();
				if(fieldType.isPrimitive()) {
					f = new PrimitiveField(description, Field.getTypeCode(fieldType), field.getName(), field);
				} else {
					f = new ObjectField(description, Field.getTypeCode(fieldType), field.getName(), fieldType.getName(), field);
				}
				fields.add(f);
			}
		}
		return fields.toArray(Field.createFields(fields.size()));
	}

	private long getVersionId(Class<?> type) throws SerializationException {
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

	private void writeDescription(Object object, SerializableDataOutputStream output, ClassDescription description) throws IOException {
		if(description.getInfo().hasFlag(ObjectStreamConstants.SC_EXTERNALIZABLE)) {
			if(WRITE_EXTERNAL == null) {
				throw new SerializationException("Could not access Externalizable.writeExternal");
			}
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			try {
				WRITE_EXTERNAL.invoke(object, new DataOutputStream(buffer));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new SerializationException(e);
			}
			if(buffer.size() > 0xFF) {
				output.writeByte(ObjectStreamConstants.TC_BLOCKDATA);
			} else {
				output.writeByte(ObjectStreamConstants.TC_BLOCKDATALONG);
			}
			output.write(buffer);
			output.writeByte(ObjectStreamConstants.TC_ENDBLOCKDATA);
		} else {
			try {
				description.getInfo().writeFieldValues(output, object);
			} catch (NoSuchFieldException | SecurityException | ClassNotFoundException | IllegalAccessException e) {
				throw new SerializationException(e);
			}
		}
	}
}
