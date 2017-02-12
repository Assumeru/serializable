package org.ee.serialization.delegates.serializable.mapper.model;

public class ClassDescription {
	private final String name;
	private final long suid;
	private ClassDescriptionInfo info;

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

	@Override
	public String toString() {
		return name + " serialVersionUID=" + suid + " " + info;
	}
}
