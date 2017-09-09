package com.sopovs.moradanen.jcstress;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.II_Result;

@JCStressTest
@Outcome(id = { "0, 0", "1, 1", "1, 2", "0, 1", "0, 2" }, expect = Expect.ACCEPTABLE)
@Outcome(id = { "1, 0" }, expect = Expect.FORBIDDEN)
@State
public class VolatileWriteReadTest {

	public int x;
	public volatile int y;

	@Actor
	public void first() {
		x = 1;
		y = 1;
		x = 2;
	}

	@Actor
	public void second(II_Result result) {
		result.r1 = y;
		result.r2 = x;
	}
}
