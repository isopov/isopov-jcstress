package com.sopovs.moradanen.jcstress.spring;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.ZZZZ_Result;
import org.springframework.util.concurrent.SettableListenableFuture;

//false, false, true, true       756,615    ACCEPTABLE  Successfully canceled                                       
//true, false, false, true       161,930     FORBIDDEN  Other cases are forbidden.                                  
//true, true, false, false    11,277,445    ACCEPTABLE  Successfully set  
@JCStressTest
@Outcome(id = "true, true, false, false", expect = Expect.ACCEPTABLE, desc = "Successfully set")
@Outcome(id = "false, false, true, true", expect = Expect.ACCEPTABLE, desc = "Successfully canceled")
@Outcome(expect = Expect.FORBIDDEN, desc = "Other cases are forbidden.")
@State
public class SettableListenableFuture2Test {

	private boolean failCallback;
	private boolean successCallback;

	private final SettableListenableFuture<String> future = new SettableListenableFuture<>();

	public SettableListenableFuture2Test() {
		future.addCallback(
				res -> successCallback = true,
				ex -> failCallback = true);
	}

	@Actor
	public void set(ZZZZ_Result r) {
		r.r1 = future.set("foo");
	}

	@Actor
	public void cancel(ZZZZ_Result r) {
		r.r3 = future.cancel(true);
	}

	@Arbiter
	public void arbiter(ZZZZ_Result r) {
		r.r2 = successCallback;
		r.r4 = failCallback;
	}

}
