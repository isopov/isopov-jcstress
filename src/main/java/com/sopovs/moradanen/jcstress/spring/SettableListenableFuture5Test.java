package com.sopovs.moradanen.jcstress.spring;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.ZZZZZ_Result;
import org.springframework.util.concurrent.SettableListenableFuture;

@JCStressTest
@Outcome(id = "true, false, false, true, false", expect = Expect.ACCEPTABLE, desc = "Successfully set")
@Outcome(id = "false, true, false, false, true", expect = Expect.ACCEPTABLE, desc = "Successfully cancel")
@Outcome(id = "false, false, true, false, true", expect = Expect.ACCEPTABLE, desc = "Successfully setException")
@Outcome(expect = Expect.FORBIDDEN, desc = "Other cases are forbidden.")
@State
public class SettableListenableFuture5Test {


	private volatile boolean failCallback;
	private volatile boolean successCallback;

	private final SettableListenableFuture<String> future = new SettableListenableFuture<>();

	public SettableListenableFuture5Test() {
		future.addCallback(res -> successCallback = true, ex -> failCallback = true);
	}
	@Actor
	public void set(ZZZZZ_Result r) {
		r.r1 = future.set("foo");
	}

	@Actor
	public void cancel(ZZZZZ_Result r) {
		r.r2 = future.cancel(true);
	}
	@Actor
	public void setException(ZZZZZ_Result r) {
		r.r3 = future.setException(new Exception());
	}

	@Arbiter
	public void arbiter(ZZZZZ_Result r) {
		r.r4 = successCallback;
		r.r5 = failCallback;
	}
	

}
