package org.ee.serialization.deserialization.serializable.mapper.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class FieldValueComparator implements Comparator<FieldValue> {
	private final ClassDescription description;
	private final Map<ClassDescription, Integer> order;

	public FieldValueComparator(ClassDescription description) {
		this.description = description;
		order = new HashMap<>();
		while(description != null) {
			order.put(description, order.size());
			description = description.getInfo().getSuperClass();
		}
	}

	@Override
	public int compare(FieldValue o1, FieldValue o2) {
		Field field1 = o1.getField();
		Field field2 = o2.getField();
		Integer depth1 = order.get(field1.getDescription());
		Integer depth2 = order.get(field2.getDescription());
		if(depth1 == null) {
			throw new IllegalArgumentException(field1 + " is not a member of " + description);
		} else if(depth2 == null) {
			throw new IllegalArgumentException(field2 + " is not a member of " + description);
		}
		int diff = depth1.compareTo(depth2);
		if(diff == 0) {
			return field1.compareTo(field2);
		}
		return diff;
	}
}
