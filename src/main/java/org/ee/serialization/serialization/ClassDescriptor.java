package org.ee.serialization.serialization;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ee.serialization.SerializationException;

public class ClassDescriptor {
	private final String name;
	private final Long version;
	private final List<Field> fields;

	private ClassDescriptor(String name, Long version, List<Field> fields) {
		this.name = name;
		this.version = version;
		this.fields = Collections.unmodifiableList(fields);
	}

	public List<Field> getFields() {
		return fields;
	}

	public String getName() {
		return name;
	}

	public Long getSerialVersion() {
		return version;
	}

	public static ClassDescriptor of(Class<?> type) throws SerializationException {
		String name = type.getName();
		ArrayList<Field> fields = new ArrayList<>();
		Long version = null;
		Class<?> first = type;
		do {
			for(Field field : type.getDeclaredFields()) {
				int modifiers = field.getModifiers();
				if(!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
					fields.add(field);
					field.setAccessible(true);
				} else if(type == first && Modifier.isStatic(modifiers) && "serialVersionUID".equals(field.getName())) {
					field.setAccessible(true);
					try {
						version = field.getLong(null);
					} catch (IllegalAccessException e) {
						throw new SerializationException("Failed to read serialVersionUID of " + type, e);
					}
				}
			}
		} while((type = type.getSuperclass()) != null);
		fields.trimToSize();
		return new ClassDescriptor(name, version, fields);
	}
}
