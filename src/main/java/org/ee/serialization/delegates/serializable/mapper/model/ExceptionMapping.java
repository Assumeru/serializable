package org.ee.serialization.delegates.serializable.mapper.model;

public class ExceptionMapping {
	private final ObjectMapping throwable;

	public ExceptionMapping(ObjectMapping throwable) {
		this.throwable = throwable;
	}

	public ObjectMapping getThrowable() {
		return throwable;
	}
}
