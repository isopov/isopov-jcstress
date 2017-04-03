package com.sopovs.moradanen.jcstress.guava;

import java.util.concurrent.atomic.AtomicInteger;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.infra.results.II_Result;

import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;

@JCStressTest
@Outcome(id = "1, 1", expect = Expect.ACCEPTABLE, desc = "start is called once")
@org.openjdk.jcstress.annotations.State
public class ServiceStartTest {
	private final SimpleService service = new SimpleService();
	private final StartingListener listener = new StartingListener();

	public ServiceStartTest() {
		service.addListener(listener, MoreExecutors.newDirectExecutorService());
	}

	@Actor
	public void start1() {
		startQuiet();
	}

	private void startQuiet() {
		try {
			service.startAsync();
		} catch (IllegalStateException ignored) {
		}
	}

	@Actor
	public void start2() {
		startQuiet();
	}

	@Arbiter
	public void arbiter(II_Result r) {
		r.r1 = service.startCount.get();
		r.r2 = listener.startingCount.get();
	}

	public static class StartingListener extends Service.Listener {
		public AtomicInteger startingCount = new AtomicInteger();

		@Override
		public void starting() {
			startingCount.incrementAndGet();
		}
	}

	public static class SimpleService extends AbstractService {
		public AtomicInteger startCount = new AtomicInteger();

		@Override
		protected void doStart() {
			startCount.incrementAndGet();
			notifyStarted();

		}

		@Override
		protected void doStop() {
			notifyStopped();
		}

	}

}
