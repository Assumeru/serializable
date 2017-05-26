package org.ee.serialization.objects;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;

public class ExternalizableObject extends SimpleObject implements Externalizable {
	private static final long serialVersionUID = -864750375306165605L;
	private String value;

	public ExternalizableObject() {
	}

	public ExternalizableObject(String name, int version, String value) {
		super(name, version);
		this.value = value;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(value);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		value = in.readUTF();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && Objects.equals(value, ((ExternalizableObject) obj).value);
	}

	@Override
	public int hashCode() {
		return super.hashCode() * 31 + Objects.hashCode(value);
	}

	@Override
	public String toString() {
		return super.toString() + " " + value;
	}
}
