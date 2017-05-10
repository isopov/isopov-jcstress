package com.sopovs.moradanen.jcstress;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.ZZ_Result;

@JCStressTest
@Outcome(id = "true, true", expect = Expect.ACCEPTABLE, desc = "Full initialization")
@Outcome(id = "true, false", expect = Expect.ACCEPTABLE_INTERESTING, desc = "No safe-publication")
@Outcome(id = "false, true", expect = Expect.ACCEPTABLE_INTERESTING, desc = "No safe-publication")
@Outcome(expect = Expect.FORBIDDEN, desc = "Other cases are forbidden.")
@State
public class DoubleCheckedLockingTest {

	private Singleton singleton;

	public Singleton getSingleton() {
		if (singleton == null)
			synchronized (this) {
				if (singleton == null)
					singleton = new Singleton();
			}
		return singleton;
	}

	@Actor
	public void first(ZZ_Result r) {
		r.r1 = getSingleton().state != null;
	}

	@Actor
	public void second(ZZ_Result r) {
		r.r2 = getSingleton().state != null;
	}

	public static class Singleton {
		public Object state;

		public Singleton() {
			state = new Object();
		}
	}

}
