package com.sopovs.moradanen.jcstress.spring;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.ZZ_Result;
import org.springframework.util.concurrent.SettableListenableFuture;

//fixed int Spring-framework 4.3.7 and 5.0.M5
@JCStressTest
@Outcome(id = "true, false", expect = Expect.ACCEPTABLE, desc = "Successfully set")
@Outcome(id = "false, true", expect = Expect.ACCEPTABLE, desc = "Successfully canceled")
@Outcome(expect = Expect.FORBIDDEN, desc = "Other cases are forbidden.")
@State
public class SettableListenableFutureTest {
  private final SettableListenableFuture<String> future = new SettableListenableFuture<String>();

  @Actor
  public void set(ZZ_Result r) {
    r.r1 = future.set("foo");
  }

  @Actor
  public void cancel(ZZ_Result r) {
    r.r2 = future.cancel(true);
  }

}
