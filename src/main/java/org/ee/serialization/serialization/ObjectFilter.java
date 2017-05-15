package org.ee.serialization.serialization;

import org.ee.serialization.Config;

public interface ObjectFilter {
	Object filter(Object object, Config config);
}
