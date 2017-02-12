package org.ee.serialization.objects;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;

public class ComplexObject implements Serializable, Iterable<ComplexObject> {
	private static final long serialVersionUID = -808145855509884445L;
	private SimpleObject value;
	private ComplexObject next;

	public ComplexObject() {
	}

	public ComplexObject(SimpleObject value, ComplexObject next) {
		this.value = value;
		this.next = next;
	}

	public SimpleObject getValue() {
		return value;
	}

	public void setValue(SimpleObject value) {
		this.value = value;
	}

	public ComplexObject getNext() {
		return next;
	}

	public void setNext(ComplexObject next) {
		this.next = next;
	}

	@Override
	public Iterator<ComplexObject> iterator() {
		return new Iterator<ComplexObject>() {
			private ComplexObject current = ComplexObject.this;

			@Override
			public boolean hasNext() {
				return current != null;
			}

			@Override
			public ComplexObject next() {
				ComplexObject out = current;
				current = current.next;
				return out;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public String toString() {
		return "Node: " + value;
	}

	@Override
	public int hashCode() {
		return 31 + Objects.hashCode(value);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		} else if(obj == null || getClass() != obj.getClass()) {
			return false;
		}
		ComplexObject other = (ComplexObject) obj;
		return Objects.equals(value, other.value);
	}
}
