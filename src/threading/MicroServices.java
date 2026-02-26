package threading;

import java.io.StringReader;
import java.util.concurrent.*;

public class MicroServices {
    public static void getLocation() {
        try{
            Thread.sleep(1000);
            System.out.println("Loaded Users location");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void getDiscounts() {
        try {
            Thread.sleep(1000);
            System.out.println("Discount service Loaded");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void userService() {
        try {
            Thread.sleep(3000);
            System.out.println("User Service loaded");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void offerService() {
        try {
            Thread.sleep(2000);
            System.out.println("Loaded offer service");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static final CountDownLatch latch = new CountDownLatch(4);


    public static void main(String[] args) {

        try {
           ExecutorService threadPool = Executors.newFixedThreadPool(4);
           threadPool.submit(()->{
                getDiscounts();
                latch.countDown();
           });
            threadPool.submit(()->{
                getLocation();
                    latch.countDown();
            });
            threadPool.submit(()->{
                userService();
                latch.countDown();
            });
            threadPool.submit(()->{
                offerService();
                latch.countDown();
            });
            threadPool.shutdown();
            latch.await(); // main thread yahi block kar dega jab tak latch ka count 0 nahi ho jata jab tak

        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            System.out.println("Main Thread is loaded");
        }
    }
}
