package com.sopovs.moradanen.jcstress.jctools;

import org.jctools.maps.NonBlockingSetInt;
import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.BooleanResult2;

@JCStressTest
@Outcome(id = "true, true", expect = Expect.ACCEPTABLE, desc = "Seeing both updates intact.")
@Outcome(expect = Expect.FORBIDDEN, desc = "Other cases are forbidden.")
@State
public class NonBlockingSetIntTest {

  private final NonBlockingSetInt bs = new NonBlockingSetInt();

  @Actor
  public void writer1() {
    bs.add(0);
  }

  @Actor
  public void writer2() {
    bs.add(1);
  }

  @Arbiter
  public void arbiter(BooleanResult2 r) {
    r.r1 = bs.contains(0);
    r.r2 = bs.contains(1);
  }
}
