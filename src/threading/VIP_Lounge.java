package threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class VIP_Lounge {
    /*
    Only three threads can enter lounger although there are 5
    and all those three three threads works for three seconds;
     */
    public static void lounge() {
        try {
            System.out.println(Thread.currentThread().getName() + " is in lounge");
            Thread.sleep(3000); // iss thread ka kaam karega
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }
    static Semaphore semaphore = new Semaphore(3);
    public static void main(String[] args) {
            ExecutorService threadPool = Executors.newFixedThreadPool(5);
        try {
            for(int i = 0; i < 5; i++) {
                threadPool.submit(()->{
                    try {
                        semaphore.acquire();
                        lounge();
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finally {
                        semaphore.release();
                    }
                });
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            threadPool.shutdown();
        }
    }
}
