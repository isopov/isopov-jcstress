package com.sopovs.moradanen.jcstress.spring;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.ZZZ_Result;
import org.springframework.util.concurrent.SettableListenableFuture;

@JCStressTest
@Outcome(id = "true, false, false", expect = Expect.ACCEPTABLE, desc = "Successfully set")
@Outcome(id = "false, true, false", expect = Expect.ACCEPTABLE, desc = "Successfully cancel")
@Outcome(id = "false, false, true", expect = Expect.ACCEPTABLE, desc = "Successfully setException")
@Outcome(expect = Expect.FORBIDDEN, desc = "Other cases are forbidden.")
@State
public class SettableListenableFuture4Test {


	private final SettableListenableFuture<String> future = new SettableListenableFuture<String>();

	@Actor
	public void set(ZZZ_Result r) {
		r.r1 = future.set("foo");
	}

	@Actor
	public void cancel(ZZZ_Result r) {
		r.r2 = future.cancel(true);
	}
	@Actor
	public void setException(ZZZ_Result r) {
		r.r3 = future.setException(new Exception());
	}

	
	

}
