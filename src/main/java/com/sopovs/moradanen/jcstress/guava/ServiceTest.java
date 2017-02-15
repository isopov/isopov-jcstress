package com.sopovs.moradanen.jcstress.guava;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;

import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.Service.State;

@JCStressTest
@Outcome(id = "false, false, false, false, null, NEW, null", expect = Expect.ACCEPTABLE, desc = "stop is called first - doStop() and doStart() are not executed, listener is only terminated from NEW state")
@Outcome(id = "true, true, true, true, RUNNING, STOPPING, null", expect = Expect.ACCEPTABLE, desc = "start is called first - both doStop() and doStart() are executed,"
		+ " listener is notified for starting, running, stopping from RUNNING state and terminating from STOPPING state")
@org.openjdk.jcstress.annotations.State
public class ServiceTest {
	private final SimpleService service = new SimpleService();
	private final SimpleListener listener = new SimpleListener();

	public ServiceTest() {
		service.addListener(listener, MoreExecutors.newDirectExecutorService());
	}

	@Actor
	public void start() {
		try {
			service.startAsync();
		} catch (IllegalStateException ignored) {
		}
	}

	@Actor
	public void stop() {
		service.stopAsync();
	}

	@Arbiter
	public void arbiter(ServiceResult r) {
		r.started = service.started;
		r.stopped = service.stopped;

		r.running = listener.running;
		r.starting = listener.starting;

		r.stopping = listener.stopping;
		r.terminated = listener.terminated;
		r.failed = listener.failed;

	}

	private static class SimpleListener extends Service.Listener {
		private volatile boolean running, starting;
		private volatile String stopping, terminated, failed;

		@Override
		public void starting() {
			starting = true;
		}

		@Override
		public void running() {
			running = true;
		}

		@Override
		public void stopping(State from) {
			stopping = from.name();
		}

		@Override
		public void terminated(State from) {
			terminated = from.name();
		}

		@Override
		public void failed(State from, Throwable failure) {
			failed = from.name();
		}

	}

	private static class SimpleService extends AbstractService {
		private volatile boolean started, stopped;

		@Override
		protected void doStart() {
			started = true;
			notifyStarted();

		}

		@Override
		protected void doStop() {
			stopped = true;
			notifyStopped();
		}

	}

}
