package org.ee.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import org.ee.serialization.objects.ComplexObject;
import org.ee.serialization.objects.SimpleObject;
import org.junit.Assert;
import org.junit.Test;

public class TestObjectInputStream {
	private final SimpleObject o1;
	private final SimpleObject o2;
	private final ComplexObject o3;
	private final byte[] buffer;

	public TestObjectInputStream() {
		o1 = new SimpleObject("s", 1);
		o2 = new SimpleObject("s", 2);
		o3 = new ComplexObject(o1, new ComplexObject(o2, null));
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try(ObjectOutputStream out = new ObjectOutputStream(buffer)) {
			out.writeObject(o1);
			out.writeObject(o2);
			out.writeObject(o3);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		this.buffer = buffer.toByteArray();
	}

	@Test
	public void testDefault() throws IOException, ClassNotFoundException {
		try(Deserializer input = new Deserializer(new ByteArrayInputStream(buffer))) {
			test(input);
		}
	}

	@Test
	public void testMapper() throws IOException, ClassNotFoundException {
		try(Deserializer input = new Deserializer(new ByteArrayInputStream(buffer), TestStandardLibrary.CONFIG)) {
			test(input);
		}
	}

	private void test(Deserializer input) throws ClassNotFoundException, IOException {
		Object d1 = input.readObject();
		Object d2 = input.readObject();
		Object d3 = input.readObject();
		Assert.assertEquals(o1, d1);
		Assert.assertEquals(o2, d2);
		Assert.assertEquals(o3, d3);
		Iterator<ComplexObject> it1 = o3.iterator();
		Iterator<ComplexObject> it2 = ((ComplexObject) d3).iterator();
		Assert.assertEquals(it1.next(), it2.next());
		Assert.assertEquals(it1.next(), it2.next());
		Assert.assertEquals(it1.hasNext(), it2.hasNext());
	}
}
