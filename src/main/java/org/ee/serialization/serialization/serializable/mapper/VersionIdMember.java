package org.ee.serialization.serialization.serializable.mapper;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class VersionIdMember implements Comparable<VersionIdMember> {
	private final String name;
	private final int modifiers;
	private final String descriptor;
	private final String sort;

	public VersionIdMember(Field field) {
		name = field.getName();
		modifiers = field.getModifiers() & (Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED
				| Modifier.STATIC | Modifier.FINAL | Modifier.VOLATILE | Modifier.TRANSIENT);
		descriptor = getFieldDescriptor(field.getType());
		sort = null;
	}

	public VersionIdMember(Constructor<?> constructor) {
		name = "<init>";
		modifiers = constructor.getModifiers() & (Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED
				| Modifier.STATIC | Modifier.FINAL | Modifier.SYNCHRONIZED
				| Modifier.NATIVE | Modifier.ABSTRACT | Modifier.STRICT);
		descriptor = getSignature(constructor.getParameterTypes(), Void.TYPE);
		sort = descriptor;
	}

	public VersionIdMember(Method method) {
		name = method.getName();
		modifiers = method.getModifiers() & (Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED
				| Modifier.STATIC | Modifier.FINAL | Modifier.SYNCHRONIZED
				| Modifier.NATIVE | Modifier.ABSTRACT | Modifier.STRICT);
		descriptor = getSignature(method.getParameterTypes(), method.getReturnType());
		sort = descriptor;
	}

	private String getFieldDescriptor(Class<?> type) {
		if(type.isPrimitive()) {
			return String.valueOf(org.ee.serialization.deserialization.serializable.mapper.model.Field.getTypeCode(type));
		}
		String fieldDescriptor = type.getName().replace('.', '/');
		if(!type.isArray()) {
			fieldDescriptor = new StringBuilder().append('L').append(fieldDescriptor).append(';').toString();
		}
		return fieldDescriptor;
	}

	private String getSignature(Class<?>[] params, Class<?> retType) {
		StringBuilder sb = new StringBuilder().append('(');
		for(Class<?> param : params) {
			sb.append(getFieldDescriptor(param));
		}
		sb.append(')').append(getFieldDescriptor(retType));
		return sb.toString().replace('/', '.');
	}

	@Override
	public int compareTo(VersionIdMember o) {
		int d = name.compareTo(o.name);
		if(d == 0 && sort != null) {
			return sort.compareTo(o.sort);
		}
		return d;
	}

	public void writeTo(DataOutputStream data) throws IOException {
		data.writeUTF(name);
		data.writeInt(modifiers);
		data.writeUTF(descriptor);
	}
}
