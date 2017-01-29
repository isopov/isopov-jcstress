package com.sopovs.moradanen.jcstress;

import java.util.concurrent.CompletableFuture;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.BooleanResult2;


@JCStressTest
@Outcome(id = "true, false", expect = Expect.ACCEPTABLE, desc = "Successfully set")
@Outcome(id = "false, true", expect = Expect.ACCEPTABLE, desc = "Successfully canceled")
@Outcome(expect = Expect.FORBIDDEN, desc = "Other cases are forbidden.")
@State
public class CompletableFutureTest {
  private final CompletableFuture<String> future = new CompletableFuture<>();

  @Actor
  public void set(BooleanResult2 r) {
    r.r1 = future.complete("foo");
  }

  @Actor
  public void cancel(BooleanResult2 r) {
    r.r2 = future.cancel(true);
  }


}
