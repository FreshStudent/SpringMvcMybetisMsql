package com.myprojct.ssm.modules.thread;

public class ThreadDemo {
	
	public static void main(String[] args){    
	    ThreadTest t = new ThreadTest(); 
	    t.run();
	    
	}
	
}

class ThreadTest implements Runnable {
	
	private int tickets = 1000000;
	
	Long currentTime = System.currentTimeMillis();

	public void run() {
		
		while (true) {
			if (tickets > 0) {
				
				System.out.println(Thread.currentThread().getName() + " is selling ticket " + tickets--);
				
			}
		}
	}
}
