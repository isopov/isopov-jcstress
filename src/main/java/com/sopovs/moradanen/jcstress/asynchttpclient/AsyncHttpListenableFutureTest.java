package com.sopovs.moradanen.jcstress.asynchttpclient;

import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.BooleanResult1;
import org.openjdk.jcstress.infra.results.BooleanResult2;

@JCStressTest
@Outcome(id = "false", expect = Expect.ACCEPTABLE, desc = "Successfully canceled")
@Outcome(expect = Expect.FORBIDDEN, desc = "Other cases are forbidden.")
@State
public class AsyncHttpListenableFutureTest {
	private static final DefaultAsyncHttpClient HTTP_CLIENT = new DefaultAsyncHttpClient();

	private final ListenableFuture<Response> future = HTTP_CLIENT.prepareGet("http://google.com").execute();

	@Actor
	public void cancel1(BooleanResult1 r) {
		cancelQuiet(r);
	}
	
	@Actor
	public void cancel2(BooleanResult1 r) {
		cancelQuiet(r);
	}

	private void cancelQuiet(BooleanResult1 r) {
		try {
			future.cancel(true);
		} catch (Exception e) {
			r.r1 = true;
		}
	}
}
