package com.iplusplus.tauseef.mttp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
/**
 * @author Ahmed
 * Custom fixed thread pool class implementation
 * depends on Worker class
 */
public class CustomThreadPool {

	private BlockingQueue<Runnable> queue;
	private List<Worker> workers;

	/**
	 * @param size
	 * initializing thread pool 
	 */
	public CustomThreadPool(int size) {
		this.queue = new ArrayBlockingQueue<>(size);
		this.workers = new ArrayList<>();

		for (int i = 0; i < size; i++) {
			Worker w = new Worker(this.queue);
			this.workers.add(w);
			Thread t = new Thread(w);
			t.start();
		}
	}

	/**
	 * @param task that implements runnable
	 *  Tasks to run in thread pool submits here
	 */
	public void submit(Runnable task) {
		try {
			queue.put(task);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * shutdown pool after finishing tasks
	 */
	public void shutDown() {
		while (!this.queue.isEmpty()) {
			// waiting for tasks to finish up
			// System.out.println("Waiting....");
		}
		for (int i = 0; i < workers.size(); i++) {
			workers.get(i).setShutDown(true);
		}
		System.out.println("Shutown completed");
	}

	/**
	 * @param args
	 * Testing method
	 */
	public static void main(String[] args) {

		// Test custom thread pool

		// Create a fixed thread pool of fixed size
		CustomThreadPool pool = new CustomThreadPool(5);

		/*pool.submit(new Employee("Tauseef", 1));
		pool.submit(new Employee("Zaki", 2));
		pool.submit(new Employee("Sameer", 3));
		pool.submit(new Employee("Arshad", 4));
		pool.submit(new Employee("Akmal", 5));
		*/
		
		// Submitting tasks in pool
		for(int i=0;i<15;i++) {
			pool.submit(new Employee(String.valueOf(i),i));
		}
		// shutdown of pool (optional)
		pool.shutDown();
	}
	
	
	/**
	 * Worker class for threads
	 *
	 */
	class Worker implements Runnable {

		private BlockingQueue<Runnable> queue;
		private volatile boolean shutDown = false;

		public Worker(BlockingQueue<Runnable> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {

			while (!shutDown) {
				try {
					Runnable task = this.queue.take();
					System.out.println("Thread name is " + Thread.currentThread().getName() + " for "
							+ ((Employee) task).getName());
					task.run();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}

		public boolean isShutDown() {
			return shutDown;
		}

		public void setShutDown(boolean shutDown) {
			this.shutDown = shutDown;
		}

	}
}

class Employee implements Runnable {

	String name;
	int id;

	Employee(String name, int id) {
		this.name = name;
		this.id = id;
	}

	@Override
	public void run() {
		// System.out.println("Starting thread " + this.name);
		Random ran = new Random();
		int delay = ran.nextInt(1000);

		// System.out.println("Delay is " + delay + " for Thread " + this.name);
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// System.out.println("Finishig thread " + this.name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
