package org.ee.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ee.serialization.deserialization.serializable.ObjectInputStreamDelegateFactory;
import org.ee.serialization.deserialization.serializable.mapper.DefaultObjectInputStreamMapper;
import org.junit.Assert;
import org.junit.Test;

public class TestStandardLibrary {
	static final Config CONFIG = new Config()
			.set(ObjectInputStreamDelegateFactory.USE_NATIVE, false)
			.set(ObjectInputStreamDelegateFactory.MAPPER, new DefaultObjectInputStreamMapper());

	@Test
	public void testArrays() throws IOException, ClassNotFoundException {
		int[][] original = new int[][] {{3, 4}, {5, 6}};
		try(Deserializer d = new Deserializer(write(original), CONFIG)) {
			int[][] parsed = (int[][]) d.readObject();
			System.out.println(parsed);
			Assert.assertEquals(original.length, parsed.length);
			for(int i = 0; i < original.length; i++) {
				Assert.assertArrayEquals(original[i], parsed[i]);
			}
		}
	}

	@Test
	public void testArrayList() throws IOException, ClassNotFoundException {
		List<?> list = Arrays.asList("1", 2, 3L, 4.0);
		test(list);
		test(new ArrayList<>(list));
		test(new ArrayList<>());
	}

	@Test
	public void testBoolean() throws IOException, ClassNotFoundException {
		test(false);
		test(true);
	}

	@Test
	public void testByte() throws IOException, ClassNotFoundException {
		test((byte) 0);
		test((byte) 123);
	}

	@Test
	public void testChar() throws IOException, ClassNotFoundException {
		test('a');
		test('0');
	}

	@Test
	public void testDate() throws IOException, ClassNotFoundException {
		test(new Date());
		test(new Date(0));
	}

	@Test
	public void testDouble() throws IOException, ClassNotFoundException {
		test(0.0);
		test(Double.NEGATIVE_INFINITY);
	}

	@Test
	public void testFloat() throws IOException, ClassNotFoundException {
		test(0.0f);
		test(Float.NEGATIVE_INFINITY);
	}

	@Test
	public void testHashMap() throws IOException, ClassNotFoundException {
		Map<Object, Object> map = new HashMap<>();
		test(map);
		map.put("hello", "world");
		map.put("age", 123);
		test(map);
	}

	@Test
	public void testHashSet() throws IOException, ClassNotFoundException {
		Set<Object> set = new HashSet<>();
		test(set);
		set.add("hello");
		set.add("world");
		test(set);
	}

	@Test
	public void testInteger() throws IOException, ClassNotFoundException {
		test(0);
		test(Integer.MAX_VALUE);
	}

	@Test
	public void testLinkedList() throws IOException, ClassNotFoundException {
		test(new LinkedList<>(Arrays.asList("1", 2, 3L, 4.0)));
		test(new LinkedList<>());
	}

	@Test
	public void testLong() throws IOException, ClassNotFoundException {
		test(0l);
		test(Long.MAX_VALUE);
	}

	@Test
	public void testShort() throws IOException, ClassNotFoundException {
		test((short) 0);
		test(Short.MAX_VALUE);
	}


	@Test
	public void testIntegerInMap() throws Exception {
		Map<String, Integer> map = new HashMap<>();
		map.put("abc", 123);
		test(map);
	}

	private void test(Object original) throws IOException, ClassNotFoundException {
		try(Deserializer d = new Deserializer(write(original), CONFIG)) {
			Object parsed = d.readObject();
			System.out.println(parsed);
			Assert.assertEquals(original, parsed);
		}
	}

	private ByteArrayInputStream write(Object object) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try(ObjectOutputStream out = new ObjectOutputStream(buffer)) {
			out.writeObject(object);
		}
		return new ByteArrayInputStream(buffer.toByteArray());
	}
}
