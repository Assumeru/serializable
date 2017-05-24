package org.ee.serialization.serialization;

import java.io.IOException;

public interface Mapper<E> {
	boolean canMap(Object object);

	void map(Object object, E output) throws IOException;
}
