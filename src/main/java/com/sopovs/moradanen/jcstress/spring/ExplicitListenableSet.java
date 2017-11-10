package com.sopovs.moradanen.jcstress.spring;

import org.springframework.util.concurrent.SettableListenableFuture;

public class ExplicitListenableSet {

	static void realMain(String[] args) throws Throwable {
		for (int i = 1; i <= 10000000; i++) {
			// System.out.print(".");
			test();
		}
	}

	static void test() throws Throwable {
		final SettableListenableFuture task = new SettableListenableFuture();

		Thread thread = new Thread() {
			public void run() {
				try {
					check(task.get() != null);
				} catch (Exception e) {
					unexpected(e);
				}
			}
		};
		thread.start();

		task.set(Boolean.TRUE);
		thread.join(5000);
	}


	// --------------------- Infrastructure ---------------------------
	static volatile int passed = 0, failed = 0;

	static void pass() {
		passed++;
	}

	static void fail() {
		failed++;
		Thread.dumpStack();
	}

	static void fail(String msg) {
		System.out.println(msg);
		fail();
	}

	static void unexpected(Throwable t) {
		failed++;
		t.printStackTrace();
	}

	static void check(boolean cond) {
		if (cond)
			pass();
		else
			fail();
	}

	static void equal(Object x, Object y) {
		if (x == null ? y == null : x.equals(y))
			pass();
		else
			fail(x + " not equal to " + y);
	}

	public static void main(String[] args) throws Throwable {
		try {
			realMain(args);
		} catch (Throwable t) {
			unexpected(t);
		}
		System.out.printf("%nPassed = %d, failed = %d%n%n", passed, failed);
		if (failed > 0)
			throw new AssertionError("Some tests failed");
	}
}