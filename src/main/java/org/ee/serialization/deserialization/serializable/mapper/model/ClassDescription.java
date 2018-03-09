package org.ee.serialization.deserialization.serializable.mapper.model;

import java.io.IOException;
import java.io.ObjectStreamConstants;
import java.util.Objects;

import org.ee.serialization.serialization.serializable.output.CachingObjectOutput;

public class ClassDescription implements ObjectOutputWriteable {
	private final String name;
	private final long suid;
	private ClassDescriptionInfo info;
	private Class<?> type;

	public ClassDescription(String name, long suid) {
		this.name = name;
		this.suid = suid;
	}

	public void setInfo(ClassDescriptionInfo info) {
		if(this.info == null) {
			this.info = info;
		}
	}

	public String getName() {
		return name;
	}

	public long getSerialVersionUID() {
		return suid;
	}

	public ClassDescriptionInfo getInfo() {
		return info;
	}

	public Class<?> getType() throws ClassNotFoundException {
		if(type == null) {
			type = Class.forName(name);
		}
		return type;
	}

	@Override
	public String toString() {
		return name + " serialVersionUID=" + suid + " " + info;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		} else if(obj instanceof ClassDescription) {
			ClassDescription other = (ClassDescription) obj;
			return suid == other.suid && Objects.equals(other.name, name);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (int) (suid ^ (suid >>> 32)) + Objects.hashCode(name);
	}

	@Override
	public void writeTo(CachingObjectOutput output) throws IOException {
		output.writeByte(ObjectStreamConstants.TC_CLASSDESC);
		output.writeUTF(name);
		output.writeLong(suid);
		output.assignHandle(this);
		info.writeTo(output);
	}
}
