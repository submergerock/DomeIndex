package org.cProc.gc;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;

public final class DirectByteBufferCleaner {
	private DirectByteBufferCleaner() {
	}

	public static void clean(final ByteBuffer byteBuffer) {
		if (!byteBuffer.isDirect())
			return;
		try {
			Object cleaner = invoke(byteBuffer, "cleaner");
			invoke(cleaner, "clean");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Object invoke(final Object target, String methodName)
			throws Exception {
		final Method method = target.getClass().getMethod(methodName);
		method.setAccessible(true);
		return method.invoke(target);

	}

}