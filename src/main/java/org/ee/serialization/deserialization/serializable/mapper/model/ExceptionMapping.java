package org.ee.serialization.deserialization.serializable.mapper.model;

public class ExceptionMapping {
	private final ObjectMapping throwable;

	public ExceptionMapping(ObjectMapping throwable) {
		this.throwable = throwable;
	}

	public ObjectMapping getThrowable() {
		return throwable;
	}
}
