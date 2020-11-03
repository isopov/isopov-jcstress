package com.sopovs.moradanen.jcstress;


import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.Z_Result;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@JCStressTest
@Outcome(id = "true", expect = Expect.ACCEPTABLE, desc = "Cached value is equal to actual")
@Outcome(expect = Expect.FORBIDDEN, desc = "Value in cache differs from actual")
@State
public class CacheUpdateTest {
    private static final Object KEY = new Object();
    private volatile Object value;
    private final Map<Object, Object> cache = new ConcurrentHashMap<>();

    @Actor
    public void set1() {
        value = new Object();
        cache.put(KEY, value);
    }

    @Actor
    public void set2() {
        value = new Object();
        cache.put(KEY, value);
    }

    @Arbiter
    public void arbiter(Z_Result r) {
        r.r1 = value == cache.get(KEY);
    }
}
