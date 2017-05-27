package org.ee.serialization.deserialization.serializable.mapper.model;

import java.io.IOException;
import java.io.ObjectStreamConstants;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;

import org.ee.serialization.serialization.json.output.JsonDataOutputStream;

public class ArrayMapping extends ObjectMapping implements List<Object>, RandomAccess {
	private static final Object[] EMPTY = {};
	private final Object[] values;

	public ArrayMapping(ClassDescription description) {
		this(description, EMPTY);
	}

	public ArrayMapping(ClassDescription description, Object[] values) {
		super(description);
		this.values = values;
	}

	@Override
	public int size() {
		return values.length;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean contains(Object o) {
		for(Object e : this) {
			if(Objects.equals(e, o)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterator<Object> iterator() {
		return listIterator();
	}

	@Override
	public Object[] toArray() {
		return toArray(new Object[size()]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		if(a.length < size()) {
			return (T[]) Arrays.copyOf((T[]) values, size(), a.getClass());
		}
		System.arraycopy(values, 0, a, 0, size());
		if(a.length > size()) {
			a[size()] = null;
		}
		return a;
	}

	@Override
	public boolean add(Object e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object e : c) {
			if(!contains(e)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends Object> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends Object> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object get(int index) {
		return values[index];
	}

	@Override
	public Object set(int index, Object element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int index, Object element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object remove(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int indexOf(Object o) {
		for(int i = 0; i < size(); i++) {
			if(Objects.equals(o, get(i))) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		for(int i = size() - 1; i >= 0; i--) {
			if(Objects.equals(o, get(i))) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public ListIterator<Object> listIterator() {
		return listIterator(0);
	}

	@Override
	public ListIterator<Object> listIterator(int index) {
		return new ListIterator<Object>() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < size();
			}

			@Override
			public Object next() {
				if(index >= size()) {
					throw new NoSuchElementException();
				}
				return get(index++);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean hasPrevious() {
				return index > 0;
			}

			@Override
			public Object previous() {
				if(index <= 0) {
					throw new NoSuchElementException();
				}
				return get(--index);
			}

			@Override
			public int nextIndex() {
				return index;
			}

			@Override
			public int previousIndex() {
				return index - 1;
			}

			@Override
			public void set(Object e) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void add(Object e) {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public List<Object> subList(int fromIndex, int toIndex) {
		return Arrays.asList(values).subList(fromIndex, toIndex);
	}

	@Override
	public void toJson(JsonDataOutputStream output) throws IOException {
		output.beginObject();
		ClassDescription description = getDescription();
		output.name(JsonDataOutputStream.PROPERTY_CLASS).value(description.getName());
		output.name(JsonDataOutputStream.PROPERTY_VERSION).value(description.getSerialVersionUID());
		output.name(JsonDataOutputStream.PROPERTY_ARRAY_VALUES).beginArray();
		for(Object value : values) {
			output.writeObject(value);
		}
		output.endArray().endObject();
	}

	@Override
	public void writeTo(CachingObjectOutput output) throws IOException {
		output.writeByte(ObjectStreamConstants.TC_ARRAY);
		output.writeObject(getDescription());
		output.assignHandle(this);
		output.writeInt(values.length);
		char type = getDescription().getName().charAt(1);
		for(Object value : values) {
			FieldValue.writeType(output, type, value);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		} else if(obj instanceof ArrayMapping) {
			ArrayMapping other = (ArrayMapping) obj;
			if(getDescription().equals(other.getDescription()) && other.size() == size()) {
				for(int i = 0; i < size(); i++) {
					if(!Objects.deepEquals(get(i), other.get(i))) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
}
