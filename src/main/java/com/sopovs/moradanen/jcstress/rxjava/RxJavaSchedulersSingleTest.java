package com.sopovs.moradanen.jcstress.rxjava;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.IntResult1;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

@JCStressTest
@Outcome(id = "220", expect = Expect.ACCEPTABLE, desc = "Successfully incremented all times")
@Outcome(expect = Expect.FORBIDDEN, desc = "Other cases are forbidden.")
@State
public class RxJavaSchedulersSingleTest {

	private int counter = 0;

	private void foobar() {
		Flowable.range(1, 10)
				.observeOn(Schedulers.single())
				.map(this::counterIncrement)
				.blockingSubscribe();
	}

	private int counterIncrement(int increment) {
		return counter += increment;
	}

	@Actor
	public void increment1() {
		foobar();
	}

	@Actor
	public void increment2() {
		foobar();
	}

	@Actor
	public void increment3() {
		foobar();
	}

	@Actor
	public void increment4() {
		foobar();
	}

	@Arbiter
	public void arbiter(IntResult1 r) {
		r.r1 = counter;
	}

}
