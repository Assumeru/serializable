package org.ee.serialization.serialization.serializable.mapper;

import java.io.IOException;
import java.io.ObjectStreamConstants;

import org.ee.serialization.serialization.serializable.output.ObjectOutputSerializer;

public class ArrayMapper implements SerializableMapper {
	private final ClassDescriptionManager cache;

	public ArrayMapper(ClassDescriptionManager cache) {
		this.cache = cache;
	}

	@Override
	public boolean canMap(Object object) {
		return object != null && object.getClass().isArray();
	}

	@Override
	public void map(Object object, ObjectOutputSerializer output) throws IOException {
		output.writeByte(ObjectStreamConstants.TC_ARRAY);
		Class<?> type = object.getClass();
		output.writeObject(cache.getClassDescription(type));
		output.assignHandle(object);
		if(type == boolean[].class) {
			write((boolean[]) object, output);
		} else if(type == byte[].class) {
			write((byte[]) object, output);
		} else if(type == char[].class) {
			write((char[]) object, output);
		} else if(type == double[].class) {
			write((double[]) object, output);
		} else if(type == float[].class) {
			write((float[]) object, output);
		} else if(type == int[].class) {
			write((int[]) object, output);
		} else if(type == long[].class) {
			write((long[]) object, output);
		} else if(type == short[].class) {
			write((short[]) object, output);
		} else {
			write((Object[]) object, output);
		}
	}

	private void write(boolean[] values, ObjectOutputSerializer output) throws IOException {
		output.writeInt(values.length);
		for(boolean value : values) {
			output.writeBoolean(value);
		}
	}

	private void write(byte[] values, ObjectOutputSerializer output) throws IOException {
		output.writeInt(values.length);
		for(byte value : values) {
			output.writeByte(value);
		}
	}

	private void write(char[] values, ObjectOutputSerializer output) throws IOException {
		output.writeInt(values.length);
		for(char value : values) {
			output.writeChar(value);
		}
	}

	private void write(double[] values, ObjectOutputSerializer output) throws IOException {
		output.writeInt(values.length);
		for(double value : values) {
			output.writeDouble(value);
		}
	}

	private void write(float[] values, ObjectOutputSerializer output) throws IOException {
		output.writeInt(values.length);
		for(float value : values) {
			output.writeFloat(value);
		}
	}

	private void write(int[] values, ObjectOutputSerializer output) throws IOException {
		output.writeInt(values.length);
		for(int value : values) {
			output.writeInt(value);
		}
	}

	private void write(long[] values, ObjectOutputSerializer output) throws IOException {
		output.writeInt(values.length);
		for(long value : values) {
			output.writeLong(value);
		}
	}

	private void write(short[] values, ObjectOutputSerializer output) throws IOException {
		output.writeInt(values.length);
		for(short value : values) {
			output.writeShort(value);
		}
	}

	private void write(Object[] values, ObjectOutputSerializer output) throws IOException {
		output.writeInt(values.length);
		for(Object value : values) {
			output.writeObject(value);
		}
	}
}
