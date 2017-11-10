package com.sopovs.moradanen.jcstress.spring;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.util.concurrent.SettableListenableFuture;

public class SettableListenableFutureMainTest {

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<SettableListenableFuture<String>> queue = new LinkedBlockingQueue<SettableListenableFuture<String>>();
		Thread thread1 = new ProducerThread(queue), thread2 = new ConsumerThread(queue);
		thread1.start();
		thread2.start();
		Thread.sleep(TimeUnit.SECONDS.toMillis(10));
		thread1.interrupt();
		thread2.interrupt();
		System.out.println("Done!");
	}

	static class ProducerThread extends Thread {
		private final BlockingQueue<SettableListenableFuture<String>> queue;

		public ProducerThread(BlockingQueue<SettableListenableFuture<String>> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			while (!this.isInterrupted()) {
				SettableListenableFuture<String> ss = new SettableListenableFuture<String>();
				ss.set("FooBar!");
				queue.offer(ss);
			}
		}
	}

	static class ConsumerThread extends Thread {
		private final BlockingQueue<SettableListenableFuture<String>> queue;

		public ConsumerThread(BlockingQueue<SettableListenableFuture<String>> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			while (!this.isInterrupted()) {
				try {
					SettableListenableFuture<String> future = queue.take();
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

}
