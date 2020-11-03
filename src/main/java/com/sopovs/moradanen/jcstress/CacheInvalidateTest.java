package com.sopovs.moradanen.jcstress;


import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.Z_Result;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@JCStressTest
@Outcome(id = "true", expect = Expect.ACCEPTABLE, desc = "Cached value is equal to actual")
@Outcome(expect = Expect.FORBIDDEN, desc = "Value in cache differs from actual")
@State
public class CacheInvalidateTest {
    private static final Object KEY = new Object();
    private volatile Object value;
    private final Map<Object, Object> cache = new ConcurrentHashMap<>();

    private Object getValue() {
        return cache.computeIfAbsent(KEY, o -> value);
    }

    @Actor
    public void set1() {
        value = new Object();
        cache.remove(KEY);
        getValue();
    }

    @Actor
    public void set2() {
        value = new Object();
        cache.remove(KEY);
        getValue();
    }

    @Arbiter
    public void arbiter(Z_Result r) {
        r.r1 = value == getValue();
    }
}
