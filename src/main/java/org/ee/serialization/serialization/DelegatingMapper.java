package org.ee.serialization.serialization;

import java.io.IOException;
import java.util.List;

public class DelegatingMapper<E> implements Mapper<E> {
	private final List<? extends Mapper<E>> mappers;

	public DelegatingMapper(List<? extends Mapper<E>> mappers) {
		this.mappers = mappers;
	}

	@Override
	public void map(Object object, E output) throws IOException {
		for(Mapper<E> mapper : mappers) {
			if(mapper.canMap(object)) {
				mapper.map(object, output);
				return;
			}
		}
	}

	@Override
	public boolean canMap(Object object) {
		for(Mapper<E> mapper : mappers) {
			if(mapper.canMap(object)) {
				return true;
			}
		}
		return false;
	}
}
