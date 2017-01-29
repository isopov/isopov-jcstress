package com.sopovs.moradanen.jcstress.spring;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.BooleanResult2;
import org.springframework.util.concurrent.SettableListenableFuture;

//Observed state   Occurrences   Expectation  Interpretation                                              
//false, true       353,611    ACCEPTABLE  Successfully canceled                                       
//true, false    12,737,382    ACCEPTABLE  Successfully set                                            
// true, true       213,887     FORBIDDEN  Other cases are forbidden.

@JCStressTest
@Outcome(id = "true, false", expect = Expect.ACCEPTABLE, desc = "Successfully set")
@Outcome(id = "false, true", expect = Expect.ACCEPTABLE, desc = "Successfully canceled")
@Outcome(expect = Expect.FORBIDDEN, desc = "Other cases are forbidden.")
@State
public class SettableListenableFutureTest {
  private final SettableListenableFuture<String> future = new SettableListenableFuture<>();

  @Actor
  public void set(BooleanResult2 r) {
    r.r1 = future.set("foo");
  }

  @Actor
  public void cancel(BooleanResult2 r) {
    r.r2 = future.cancel(true);
  }

}
