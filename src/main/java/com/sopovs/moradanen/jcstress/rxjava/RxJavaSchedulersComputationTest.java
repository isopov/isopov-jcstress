package com.sopovs.moradanen.jcstress.rxjava;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.I_Result;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

@JCStressTest
@Outcome(id = "4", expect = Expect.ACCEPTABLE, desc = "Successfully incremented all times")
@Outcome(id = "3", expect = Expect.ACCEPTABLE_INTERESTING, desc = "Successfully incremented 3 times")
@Outcome(id = "2", expect = Expect.ACCEPTABLE_INTERESTING, desc = "Successfully incremented 2 times")
@Outcome(id = "1", expect = Expect.ACCEPTABLE_INTERESTING, desc = "Successfully incremented 1 time")
@Outcome(expect = Expect.FORBIDDEN, desc = "Other cases are forbidden.")
@State
public class RxJavaSchedulersComputationTest {

	private int counter = 0;

	private void foobar() {
		Flowable.just(1)
				.observeOn(Schedulers.computation())
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
	public void arbiter(I_Result r) {
		r.r1 = counter;
	}

}
