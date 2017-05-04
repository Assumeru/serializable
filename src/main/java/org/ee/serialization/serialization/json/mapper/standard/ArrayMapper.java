package org.ee.serialization.serialization.json.mapper.standard;

import java.io.IOException;

import org.ee.serialization.serialization.json.mapper.JsonMapper;
import org.ee.serialization.serialization.json.output.JsonDataOutputStream;

public class ArrayMapper implements JsonMapper {
	@Override
	public void map(Object object, JsonDataOutputStream output) throws IOException {
		if(object.getClass() == boolean[].class) {
			writeArray((boolean[]) object, output);
		} else if(object.getClass() == byte[].class) {
			writeArray((byte[]) object, output);
		} else if(object.getClass() == char[].class) {
			writeArray((char[]) object, output);
		} else if(object.getClass() == double[].class) {
			writeArray((double[]) object, output);
		} else if(object.getClass() == float[].class) {
			writeArray((float[]) object, output);
		} else if(object.getClass() == int[].class) {
			writeArray((int[]) object, output);
		} else if(object.getClass() == long[].class) {
			writeArray((long[]) object, output);
		} else if(object.getClass() == short[].class) {
			writeArray((short[]) object, output);
		} else if(object.getClass().isArray()) {
			writeArray((Object[]) object, output);
		}
	}

	@Override
	public boolean canMap(Object object) {
		if(object == null) {
			return false;
		}
		return object.getClass().isArray();
	}


	private void writeArray(boolean[] array, JsonDataOutputStream output) throws IOException {
		output.beginObject();
		output.name(JsonDataOutputStream.PROPERTY_CLASS).value(boolean[].class.getName());
		output.name(JsonDataOutputStream.PROPERTY_ARRAY_VALUES).beginArray();
		for(boolean value : array) {
			output.value(value);
		}
		output.endArray();
		output.endObject();
	}

	private void writeArray(byte[] array, JsonDataOutputStream output) throws IOException {
		output.beginObject();
		output.name(JsonDataOutputStream.PROPERTY_CLASS).value(byte[].class.getName());
		output.name(JsonDataOutputStream.PROPERTY_ARRAY_VALUES).beginArray();
		for(byte value : array) {
			output.value(value);
		}
		output.endArray();
		output.endObject();
	}

	private void writeArray(char[] array, JsonDataOutputStream output) throws IOException {
		output.beginObject();
		output.name(JsonDataOutputStream.PROPERTY_CLASS).value(char[].class.getName());
		output.name(JsonDataOutputStream.PROPERTY_ARRAY_VALUES).beginArray();
		for(char value : array) {
			output.value(value);
		}
		output.endArray();
		output.endObject();
	}

	private void writeArray(double[] array, JsonDataOutputStream output) throws IOException {
		output.beginObject();
		output.name(JsonDataOutputStream.PROPERTY_CLASS).value(double[].class.getName());
		output.name(JsonDataOutputStream.PROPERTY_ARRAY_VALUES).beginArray();
		for(double value : array) {
			output.value(value);
		}
		output.endArray();
		output.endObject();
	}

	private void writeArray(float[] array, JsonDataOutputStream output) throws IOException {
		output.beginObject();
		output.name(JsonDataOutputStream.PROPERTY_CLASS).value(float[].class.getName());
		output.name(JsonDataOutputStream.PROPERTY_ARRAY_VALUES).beginArray();
		for(float value : array) {
			output.value(value);
		}
		output.endArray();
		output.endObject();
	}

	private void writeArray(int[] array, JsonDataOutputStream output) throws IOException {
		output.beginObject();
		output.name(JsonDataOutputStream.PROPERTY_CLASS).value(int[].class.getName());
		output.name(JsonDataOutputStream.PROPERTY_ARRAY_VALUES).beginArray();
		for(int value : array) {
			output.value(value);
		}
		output.endArray();
		output.endObject();
	}

	private void writeArray(long[] array, JsonDataOutputStream output) throws IOException {
		output.beginObject();
		output.name(JsonDataOutputStream.PROPERTY_CLASS).value(long[].class.getName());
		output.name(JsonDataOutputStream.PROPERTY_ARRAY_VALUES).beginArray();
		for(long value : array) {
			output.value(value);
		}
		output.endArray();
		output.endObject();
	}

	private void writeArray(short[] array, JsonDataOutputStream output) throws IOException {
		output.beginObject();
		output.name(JsonDataOutputStream.PROPERTY_CLASS).value(short[].class.getName());
		output.name(JsonDataOutputStream.PROPERTY_ARRAY_VALUES).beginArray();
		for(short value : array) {
			output.value(value);
		}
		output.endArray();
		output.endObject();
	}

	private void writeArray(Object[] array, JsonDataOutputStream output) throws IOException {
		output.beginObject();
		output.name(JsonDataOutputStream.PROPERTY_CLASS).value(array.getClass().getName());
		output.name(JsonDataOutputStream.PROPERTY_ARRAY_VALUES).beginArray();
		for(Object value : array) {
			output.writeObject(value);
		}
		output.endArray();
		output.endObject();
	}
}
