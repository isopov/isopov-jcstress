package com.sopovs.moradanen.jcstress.spring;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.util.concurrent.SettableListenableFuture;

public class SettableListenableFutureMainTest {

	public static void main(String[] args) {
		BlockingQueue<ResultFutureContainer> queue1 = new LinkedBlockingQueue<ResultFutureContainer>(), queue2 = new LinkedBlockingQueue<ResultFutureContainer>();
		Thread thread1 = new SuccessThread(queue1), thread2 = new ExceptionThread(queue2);
		thread1.start();
		thread2.start();
		for (int i = 0; i < 1000; i++) {
			ResultFutureContainer container = new ResultFutureContainer();
			queue1.add(container);
			queue2.add(container);
			while (!container.exceptionRun || !container.successRun) {
				Thread.yield();
			}
			if (container.result.sucessCallback && !container.result.success) {
				System.out.println("SuccessCallback without success!");
			}
			if (container.result.failCallback && !container.result.exception) {
				System.out.println("FailCallback without fail!");
			}
		}
		thread1.interrupt();
		thread2.interrupt();
	}

	public static class Result {
		public volatile boolean success, sucessCallback, exception, failCallback;
	}

	public static class ResultFutureContainer {
		public final Result result = new Result();
		public volatile boolean successRun, exceptionRun;
		public final SettableListenableFuture<String> future = new SettableListenableFuture<>();

		public ResultFutureContainer() {
			future.addCallback(res -> result.sucessCallback = true, ex -> result.failCallback = true);
		}
	}

	static class SuccessThread extends Thread {
		private final BlockingQueue<ResultFutureContainer> queue;

		public SuccessThread(BlockingQueue<ResultFutureContainer> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			while (!this.isInterrupted()) {
				try {
					ResultFutureContainer container = queue.take();
					container.result.success = container.future.set("foo");
					container.successRun = true;
				} catch (InterruptedException e) {
					this.interrupt();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	static class ExceptionThread extends Thread {
		private final BlockingQueue<ResultFutureContainer> queue;

		public ExceptionThread(BlockingQueue<ResultFutureContainer> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			while (!this.isInterrupted()) {
				try {
					ResultFutureContainer container = queue.take();
					container.result.exception = container.future.setException(new Exception());
					container.exceptionRun = true;
				} catch (InterruptedException e) {
					this.interrupt();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
