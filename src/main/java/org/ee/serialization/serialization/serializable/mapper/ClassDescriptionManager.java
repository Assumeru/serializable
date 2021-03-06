package org.ee.serialization.serialization.serializable.mapper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamConstants;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ee.serialization.SerializationException;
import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescription;
import org.ee.serialization.deserialization.serializable.mapper.model.ClassDescriptionInfo;
import org.ee.serialization.deserialization.serializable.mapper.model.Field;
import org.ee.serialization.deserialization.serializable.mapper.model.ObjectField;
import org.ee.serialization.deserialization.serializable.mapper.model.PrimitiveField;
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

	public ClassDescription getClassDescription(Class<?> type, boolean sid) throws SerializationException {
		ClassDescription description = classDescriptions.get(type);
		if(description == null) {
			if(Proxy.isProxyClass(type)) {
				//TODO proxy classes
				throw new SerializationException("No support for proxy classes: " + type);
			}
			description = new ClassDescription(type.getName(), sid ? getVersionId(type) : 0);
			classDescriptions.put(type, description);
			byte flags = getFlags(type);
			Field[] fields = getFields(type, description);
			Class<?> superClass = type.getSuperclass();
			ClassDescriptionInfo info = new ClassDescriptionInfo(flags, fields, Collections.emptyList(), superClass != null && Serializable.class.isAssignableFrom(superClass) ? getClassDescription(superClass) : null);
			description.setInfo(info);
		}
		return description;
	}

	public ClassDescription getClassDescription(Class<?> type) throws SerializationException {
		return getClassDescription(type, true);
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
					String fieldDescriptor = fieldType.getName().replace('.', '/');
					if(!fieldType.isArray()) {
						fieldDescriptor = new StringBuilder().append('L').append(fieldDescriptor).append(';').toString();
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
		if(Enum.class.isAssignableFrom(type) || Proxy.isProxyClass(type)) {
			return 0;
		}
		try {
			try {
				java.lang.reflect.Field field = type.getDeclaredField("serialVersionUID");
				field.setAccessible(true);
				return field.getLong(null);
			} catch (NoSuchFieldException | NullPointerException e) {
				return computeVersionId(type);
			}
		} catch (SecurityException | IllegalAccessException | IOException | NoSuchAlgorithmException e) {
			throw new SerializationException(e);
		}
	}

	public void writeFromDescription(Object object, ObjectOutputSerializer output) throws IOException {
		output.writeByte(ObjectStreamConstants.TC_OBJECT);
		ClassDescription description = getClassDescription(object.getClass());
		output.writeObject(description);
		output.assignHandle(object);
		Deque<ClassDescription> stack = new LinkedList<>();
		while(description != null) {
			stack.push(description);
			if(description.getInfo().hasFlag(ObjectStreamConstants.SC_EXTERNALIZABLE)) {
				break;
			}
			description = description.getInfo().getSuperClass();
		}
		while(!stack.isEmpty()) {
			description = stack.pop();
			writeFromDescription(object, output, description);
		}
	}

	private void writeFromDescription(Object object, ObjectOutputSerializer output, ClassDescription description) throws IOException {
		if(description.getInfo().hasFlag(ObjectStreamConstants.SC_EXTERNALIZABLE)) {
			if(WRITE_EXTERNAL == null) {
				throw new SerializationException("Could not access Externalizable.writeExternal");
			}
			try(StreamBuffer buffer = output.getBlockDataBuffer()) {
				WRITE_EXTERNAL.invoke(object, buffer);
			} catch(InvocationTargetException e) {
				throw new SerializationException("writeExternal threw an exception for " + description, e.getCause());
			} catch (IllegalAccessException | IllegalArgumentException e) {
				throw new SerializationException(e);
			}
			output.writeByte(ObjectStreamConstants.TC_ENDBLOCKDATA);
		}
		try {
			description.getInfo().writeFieldValues(output, object);
		} catch (NoSuchFieldException | SecurityException | ClassNotFoundException | IllegalAccessException e) {
			throw new SerializationException(e);
		}
	}

	private long computeVersionId(Class<?> type) throws IOException, NoSuchAlgorithmException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(bytes);
		data.writeUTF(type.getName());
		data.writeInt(type.getModifiers() & (Modifier.PUBLIC | Modifier.FINAL | Modifier.INTERFACE | Modifier.ABSTRACT));
		writeInterfaces(type, data);
		writeFields(type, data);
		//TODO static initializer
		writeConstructors(type, data);
		writeMethods(type, data);
		MessageDigest md = MessageDigest.getInstance("SHA");
		byte[] hash = md.digest(bytes.toByteArray());
		long id = 0;
		for (int i = Math.min(hash.length, 8) - 1; i >= 0; i--) {
			id = (id << 8) | (hash[i] & 0xFF);
		}
		return id;
	}

	private void writeInterfaces(Class<?> type, DataOutputStream data) throws IOException {
		if(!type.isArray()) {
			Class<?>[] interfaces = type.getInterfaces();
			String[] names = new String[interfaces.length];
			for(int i = 0; i < names.length; i++) {
				names[i] = interfaces[i].getName();
			}
			Arrays.sort(names);
			for(String name : names) {
				data.writeUTF(name);
			}
		}
	}

	private void writeFields(Class<?> type, DataOutputStream data) throws IOException {
		List<VersionIdMember> fields = new ArrayList<>();
		for(java.lang.reflect.Field field : type.getDeclaredFields()) {
			int modifiers = field.getModifiers();
			if(!Modifier.isPrivate(modifiers) || (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers))) {
				fields.add(new VersionIdMember(field));
			}
		}
		Collections.sort(fields);
		for(VersionIdMember field : fields) {
			field.writeTo(data);
		}
	}

	private void writeConstructors(Class<?> type, DataOutputStream data) throws IOException {
		List<VersionIdMember> constructors = new ArrayList<>();
		for(Constructor<?> constructor : type.getDeclaredConstructors()) {
			if(!Modifier.isPrivate(constructor.getModifiers())) {
				constructors.add(new VersionIdMember(constructor));
			}
		}
		Collections.sort(constructors);
		for(VersionIdMember constructor : constructors) {
			constructor.writeTo(data);
		}
	}

	private void writeMethods(Class<?> type, DataOutputStream data) throws IOException {
		List<VersionIdMember> methods = new ArrayList<>();
		for(Method method : type.getDeclaredMethods()) {
			if(!Modifier.isPrivate(method.getModifiers())) {
				methods.add(new VersionIdMember(method));
			}
		}
		Collections.sort(methods);
		for(VersionIdMember method : methods) {
			method.writeTo(data);
		}
	}
}
