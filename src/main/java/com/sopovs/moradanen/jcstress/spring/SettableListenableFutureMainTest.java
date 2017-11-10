package com.sopovs.moradanen.jcstress.spring;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SettableListenableFutureMainTest {

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<SettableFuture<String>> queue = new LinkedBlockingQueue<SettableFuture<String>>();
		Thread thread1 = new ProducerThread(queue), thread2 = new ConsumerThread(queue);
		thread1.start();
		thread2.start();
		Thread.sleep(TimeUnit.SECONDS.toMillis(10));
		thread1.interrupt();
		thread2.interrupt();
		System.out.println("Done!");
	}

	static class ProducerThread extends Thread {
		private final BlockingQueue<SettableFuture<String>> queue;

		public ProducerThread(BlockingQueue<SettableFuture<String>> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			while (!this.isInterrupted()) {
				SettableFuture<String> ss = new SettableFuture<String>();
				ss.set("FooBar!");
				queue.offer(ss);
			}
		}
	}

	static class ConsumerThread extends Thread {
		private final BlockingQueue<SettableFuture<String>> queue;

		public ConsumerThread(BlockingQueue<SettableFuture<String>> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			while (!this.isInterrupted()) {
				try {
					SettableFuture<String> future = queue.take();
					if (null == future.get()) {
						System.out.println("WAT!");
						return;
					}
				} catch (InterruptedException e) {
					this.interrupt();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static class SettableFuture<V> extends FutureTask<V> {
		private static final Callable<Object> DUMMY = new Callable<Object>() {

			@Override
			public Object call() throws Exception {
				throw new RuntimeException("Should not be called");
			}
		};

		public SettableFuture() {
			super((Callable<V>) DUMMY);
		}

		public V get() throws InterruptedException, ExecutionException {
			return super.get();
		}

		public void set(V value) {
			super.set(value);
		}
	}

}
