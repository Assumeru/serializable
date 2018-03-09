package org.ee.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.ee.serialization.objects.ComplexObject;
import org.ee.serialization.objects.ExternalizableObject;
import org.ee.serialization.objects.SimpleEnum;
import org.ee.serialization.objects.SimpleObject;
import org.ee.serialization.serialization.serializable.ObjectOutputStreamSerializer;
import org.ee.serialization.util.GuardedOutputStream;
import org.junit.Assert;
import org.junit.Test;

public class TestObjectOutputStream {
	private final SimpleObject o1;
	private final SimpleObject o2;
	private final ComplexObject o3;
	private final Date date;
	private final ByteArrayOutputStream buffer;

	public TestObjectOutputStream() {
		o1 = new SimpleObject("simple 1", 1);
		o2 = new SimpleObject("simple 2", 2);
		o3 = new ComplexObject(o1, new ComplexObject(o2, null));
		date = new Date();
		buffer = new ByteArrayOutputStream();
	}

	@Test
	public void test() throws IOException, ClassNotFoundException {
		test(o1, o2, o3);
	}

	@Test
	public void testDate() throws ClassNotFoundException, IOException {
		test(date, date, new ArrayList<>(Arrays.asList(date)));
	}

	@Test
	public void testMap() throws ClassNotFoundException, IOException {
		Map<Date, Date> map = new HashMap<>();
		map.put(date, date);
		test(map, map);
	}

	@Test
	public void testArray() throws ClassNotFoundException, IOException {
		int[] array = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
		byte[] custom = getCustomOutput(array);
		try(ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(custom))) {
			int[] read = (int[]) in.readObject();
			Assert.assertArrayEquals(array, read);
		}
	}

	@Test
	public void testSid() throws ClassNotFoundException, IOException {
		test(new Object[] { new SimpleObject[] { o1, o2 } });
	}

	@Test
	public void testClass() throws ClassNotFoundException, IOException {
		Class<?> type = Object.class;
		test(type);
	}

	@Test
	public void testEnum() throws ClassNotFoundException, IOException {
		test(SimpleEnum.A, SimpleEnum.B, SimpleEnum.C);
	}

	@Test
	public void testExternalizable() throws ClassNotFoundException, IOException {
		ExternalizableObject o1 = new ExternalizableObject(null, 0, "1234");
		ExternalizableObject o2 = new ExternalizableObject(null, 0, "5678");
		test(o1, o2);
	}

	private void test(Object... objects) throws IOException, ClassNotFoundException {
		byte[] normal = getStandardOutput(objects);
		byte[] custom = getCustomOutput(objects);
		try(OutputStream out = new GuardedOutputStream(System.out)) {
			out.write(normal);
			System.out.println();
			out.write(custom);
		}
		System.out.println();
		System.out.println(Arrays.toString(normal));
		System.out.println(Arrays.toString(custom));
		try(Deserializer input = new Deserializer(new ByteArrayInputStream(custom), TestStandardLibrary.CONFIG)) {
			for(Object o : objects) {
				Object read = input.readObject();
				if(!o.equals(read)) {
					System.err.println("NE: " + read);
				}
			}
		}
		Assert.assertArrayEquals(normal, custom);
	}

	private byte[] getStandardOutput(Object... objects) throws IOException {
		try(ObjectOutputStream out = new ObjectOutputStream(buffer)) {
			for(Object o : objects) {
				out.writeObject(o);
			}
		}
		byte[] out = buffer.toByteArray();
		buffer.reset();
		return out;
	}

	private byte[] getCustomOutput(Object... objects) throws IOException {
		try(Serializer out = new ObjectOutputStreamSerializer(buffer, new Config())) {
			for(Object o : objects) {
				out.writeObject(o);
			}
		}
		byte[] out = buffer.toByteArray();
		buffer.reset();
		return out;
	}
}
