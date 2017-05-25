package org.ee.serialization;

import java.io.IOException;
import java.io.OutputStream;

import org.ee.serialization.deserialization.serializable.ObjectInputStreamDelegateFactory;
import org.ee.serialization.objects.ComplexObject;
import org.ee.serialization.objects.SimpleObject;
import org.ee.serialization.serialization.json.JsonSerializer;
import org.ee.serialization.util.GuardedOutputStream;
import org.junit.Test;

public class TestJsonOutput {
	static final Config CONFIG = new Config()
			.set(ObjectInputStreamDelegateFactory.USE_NATIVE, false)
			.set(JsonSerializer.PRETTY_PRINT, true);
	static final OutputStream STDOUT = new GuardedOutputStream(System.out);
	private final SimpleObject o1;
	private final SimpleObject o2;
	private final ComplexObject o3;

	public TestJsonOutput() {
		o1 = new SimpleObject("s", 1);
		o2 = new SimpleObject("s", 2);
		o3 = new ComplexObject(o1, new ComplexObject(o2, null));
	}

	@Test
	public void test() throws IOException {
		try(Serializer serializer = new JsonSerializer(STDOUT, CONFIG)) {
			serializer.writeObject(new Object[] { o1, o2, o3 });
		}
	}
}
