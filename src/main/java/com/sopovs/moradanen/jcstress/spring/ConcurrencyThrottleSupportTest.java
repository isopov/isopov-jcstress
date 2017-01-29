package com.sopovs.moradanen.jcstress.spring;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.IntResult1;
import org.springframework.util.ConcurrencyThrottleSupport;

@SuppressWarnings("serial")
@JCStressTest
@Outcome(id = "2", expect = Expect.ACCEPTABLE, desc = "Seeing both updates intact.")
@Outcome(expect = Expect.FORBIDDEN, desc = "Other cases are forbidden.")
@State
public class ConcurrencyThrottleSupportTest extends ConcurrencyThrottleSupport {

  private int counter = 0;

  public ConcurrencyThrottleSupportTest() {
    setConcurrencyLimit(1);
  }

  private void increment() {
    beforeAccess();
    counter++;
    afterAccess();
  }

  @Actor
  public void writer1() {
    increment();
  }

  @Actor
  public void writer2() {
    increment();
  }

  @Arbiter
  public void arbiter(IntResult1 r) {
    r.r1 = counter;
  }

}
