package threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


public class RateLimiter {
    private static final Semaphore semaphore = new Semaphore(3);
    private static void fun() {
        try {
            semaphore.acquire();
            System.out.print(" " + Thread.currentThread().getName() + " ");
            Thread.sleep(1000);
//            semaphore.release();  --> wrong, sleep me crash  ho gya to deadlock
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            System.out.println();
            semaphore.release();
        }
    }
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            threadPool.submit(RateLimiter::fun);
        }
        threadPool.shutdown();
    }
}
