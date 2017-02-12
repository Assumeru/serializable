package org.ee.serialization.delegates.serializable.mapper.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ee.serialization.delegates.serializable.ObjectInputStreamMapperDelegate;

public class ClassDescriptionInfo {
	private final byte flags;
	private final Field[] fields;
	private final List<Object> annotation;
	private final ClassDescription superClass;

	public ClassDescriptionInfo(byte flags, Field[] fields, List<Object> annotation, ClassDescription superClass) {
		this.flags = flags;
		this.fields = fields;
		this.annotation = annotation;
		this.superClass = superClass;
	}

	public boolean hasFlag(byte flag) {
		return (flags & flag) == flag;
	}

	public byte getFlags() {
		return flags;
	}

	public int getFields() {
		return fields.length;
	}

	public ClassDescription getSuperClass() {
		return superClass;
	}

	void readFieldValues(ObjectInputStreamMapperDelegate input, List<FieldValue> fieldValues) throws IOException {
		for(Field field : fields) {
			fieldValues.add(new FieldValue(field, input.readType(field.getTypeCode())));
		}
	}

	@Override
	public String toString() {
		return "INFO<" + flags + " " + Arrays.toString(fields) + " " + annotation + " " + superClass + ">";
	}
}
