package org.ee.serialization.serialization;

import java.io.IOException;
import java.util.List;

public class DelegatingMapper<E, T extends Mapper<E>> implements Mapper<E> {
	protected final List<T> mappers;

	public DelegatingMapper(List<T> mappers) {
		this.mappers = mappers;
	}

	@Override
	public void map(Object object, E output) throws IOException {
		for(T mapper : mappers) {
			if(mapper.canMap(object)) {
				mapper.map(object, output);
				return;
			}
		}
	}

	@Override
	public boolean canMap(Object object) {
		for(T mapper : mappers) {
			if(mapper.canMap(object)) {
				return true;
			}
		}
		return false;
	}
}
