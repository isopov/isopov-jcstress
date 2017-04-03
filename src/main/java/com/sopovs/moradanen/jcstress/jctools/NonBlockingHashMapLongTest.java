package com.sopovs.moradanen.jcstress.jctools;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jctools.maps.NonBlockingHashMapLong;
import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.LL_Result;

@JCStressTest
@Outcome(expect = FORBIDDEN, desc = "Other cases are forbidden.")
@Outcome(id = {"Bar, Baz", "Baz, Bar"}, expect = ACCEPTABLE, desc = "Both updates.")
@State
public class NonBlockingHashMapLongTest {

  private final NonBlockingHashMapLong<List<String>> map = new NonBlockingHashMapLong<>();

  @Actor
  public void actor1() {
    addCorrect(0L, "Bar");
  }

  @Actor
  public void actor2() {
    addCorrect(0L, "Baz");
  }

  private void addCorrect(long key, String val) {
    List<String> list = map.computeIfAbsent(key,
        k -> Collections.synchronizedList(new ArrayList<>()));
    list.add(val);
  }

  private String poll(long key, int idx) {
    List<String> list = map.get(key);
    return (list.size() > idx) ? list.get(idx) : null;
  }

  @Arbiter
  public void arbiter(LL_Result s) {
    s.r1 = poll(0L, 0);
    s.r2 = poll(0L, 1);
  }

}
