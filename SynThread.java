public class SynThread implements Runnable {
    static class Lock {
	private boolean isLocked = false;

	public synchronized void lock() throws InterruptedException {
	    while (isLocked) {
		wait();
	    }
	    isLocked = true;
	}

	public synchronized void unlock() {
	    isLocked = false;
	    notify();
	}
    }
    
    private Counter counter;
    private String name;
    public SynThread(String name, Counter counter) {
	this.name = name;
	this.counter = counter;
    }
    
    public void run() {
	boolean success = false;
	try {
	    counter.lock.lock();
	    success = true;
	    for (int i = 0; i < 10; i++) {
		System.out.println(name + ":" + counter.inc());
	    }
	} catch (Exception e) {

	} finally {
	    if (success)
		counter.lock.unlock();
	}

	/*
	synchronized(counter) {
	    for (int i = 0; i < 10; i++) {
		System.out.println(name + ":" + counter.inc());
	    }
	}
	*/
    }

    static class Counter {
	Lock lock = new Lock();
	private int count = 0;
	
	public int inc() {
	    return ++count;
	}
    }

    public static void main(String[] args) {
	Counter counter = new Counter();
	SynThread s1 = new SynThread("s1", counter);
	SynThread s2 = new SynThread("s2", counter);
	Thread t1 = new Thread(s1);
	Thread t2 = new Thread(s2);
	t1.start();
	t2.start();

	try {
	    t1.join();
	    t2.join();
	} catch (Exception e) {

	}
    }
}
