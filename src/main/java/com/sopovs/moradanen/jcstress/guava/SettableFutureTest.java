package com.sopovs.moradanen.jcstress.guava;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.ZZ_Result;

import com.google.common.util.concurrent.SettableFuture;

@JCStressTest
@Outcome(id = "true, false", expect = Expect.ACCEPTABLE, desc = "Successfully set")
@Outcome(id = "false, true", expect = Expect.ACCEPTABLE, desc = "Successfully canceled")
@Outcome(expect = Expect.FORBIDDEN, desc = "Other cases are forbidden.")
@State
public class SettableFutureTest {
  private final SettableFuture<String> future = SettableFuture.create();

  @Actor
  public void set(ZZ_Result r) {
    r.r1 = future.set("foo");
  }

  @Actor
  public void cancel(ZZ_Result r) {
    r.r2 = future.cancel(true);
  }

}
