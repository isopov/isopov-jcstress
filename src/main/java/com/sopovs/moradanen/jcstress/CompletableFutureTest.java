package com.sopovs.moradanen.jcstress;

import java.util.concurrent.CompletableFuture;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
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
  private boolean set;
  private boolean cancel;

  @Actor
  public void set() {
    set = future.complete("foo");
  }

  @Actor
  public void cancel() {
    cancel = future.cancel(true);
  }

  @Arbiter
  public void arbiter(BooleanResult2 r) {
    r.r1 = set;
    r.r2 = cancel;
  }

}
