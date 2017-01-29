package com.sopovs.moradanen.jcstress.guava;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.BooleanResult2;

import com.google.common.util.concurrent.SettableFuture;


@JCStressTest
@Outcome(id = "true, false", expect = Expect.ACCEPTABLE, desc = "Successfully set")
@Outcome(id = "false, true", expect = Expect.ACCEPTABLE, desc = "Successfully canceled")
@Outcome(expect = Expect.FORBIDDEN, desc = "Other cases are forbidden.")
@State
public class SettableFutureTest {
  private final SettableFuture<String> future = SettableFuture.create();
  private boolean set;
  private boolean cancel;

  @Actor
  public void set() {
    set = future.set("foo");
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
