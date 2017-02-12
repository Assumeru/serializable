package org.ee.serialization.delegates.serializable.mapper.standard;

import java.io.IOException;
import java.util.Date;

import org.ee.serialization.delegates.serializable.mapper.ObjectInputStreamMapper;
import org.ee.serialization.delegates.serializable.mapper.model.ObjectMapping;

public class DateMapper extends AbstractMapper<Date> {
	public DateMapper() {
		super(Date.class);
	}

	@Override
	protected Date doMap(ObjectMapping object, ObjectInputStreamMapper mapper) throws IOException {
		return new Date(object.getData().readLong());
	}
}
