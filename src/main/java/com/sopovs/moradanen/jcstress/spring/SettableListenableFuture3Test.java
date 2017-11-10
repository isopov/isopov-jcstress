package com.sopovs.moradanen.jcstress.spring;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.ZZZZ_Result;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SettableListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;

//false, false, false, true         1,085     FORBIDDEN  Other cases are forbidden.                                  
//false, false, true, true          7,113    ACCEPTABLE  Successfully canceled                                       
//false, true, false, false        46,427     FORBIDDEN  Other cases are forbidden.                                  
//true, true, false, false        356,265    ACCEPTABLE  Successfully set  
@JCStressTest
@Outcome(id = "true, true, false, false", expect = Expect.ACCEPTABLE, desc = "Successfully set")
@Outcome(id = "false, false, true, true", expect = Expect.ACCEPTABLE, desc = "Successfully canceled")
@Outcome(expect = Expect.FORBIDDEN, desc = "Other cases are forbidden.")
@State
public class SettableListenableFuture3Test {

	private volatile boolean failCallback;
	private volatile boolean successCallback;

	private final SettableListenableFuture<String> future = new SettableListenableFuture<String>();

	public SettableListenableFuture3Test() {
		future.addCallback(
				new SuccessCallback<String>() {

					@Override
					public void onSuccess(String result) {
						successCallback = true;

					}
				},
				new FailureCallback() {

					@Override
					public void onFailure(Throwable ex) {
						failCallback = true;

					}

				});
	}

	@Actor
	public void set(ZZZZ_Result r) {
		r.r1 = future.set("foo");
	}

	@Actor
	public void cancel(ZZZZ_Result r) {
		r.r3 = future.setException(new Exception());
	}

	@Arbiter
	public void arbiter(ZZZZ_Result r) {
		r.r2 = successCallback;
		r.r4 = failCallback;
	}
	
	

}
