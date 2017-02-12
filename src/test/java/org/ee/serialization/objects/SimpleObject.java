package org.ee.serialization.objects;

import java.io.Serializable;
import java.util.Objects;

public class SimpleObject implements Serializable {
	private static final long serialVersionUID = 7725035533430680969L;
	private String name;
	private int version;

	public SimpleObject() {
	}

	public SimpleObject(String name, int version) {
		this.name = name;
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return name + " version " + version;
	}

	@Override
	public int hashCode() {
		return 961 + Objects.hashCode(name) * 31 + version;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		} else if(obj == null || getClass() != obj.getClass()) {
			return false;
		}
		SimpleObject other = (SimpleObject) obj;
		return version == other.version && Objects.equals(name, other.name);
	}
}
