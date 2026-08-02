package threading;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.*;

class User {
    public int userNumber;
    public User(int id) {
        this.userNumber = id;
    }
}
/*
    Problem : Make an api that process 3 users payment request concurrently
 */
public class ZomatoAPI {
    private static final BlockingQueue<User> blockingQueue =  new ArrayBlockingQueue<>(100000); //itne users aa jao hum handle kar lenge
    public static final Semaphore sempahore = new Semaphore(3);
    public static void makePayment(String user, String threadName) {
        try{
            System.out.println(LocalDateTime.now() + " " + user + " is making Payment" + "with thread " + Thread.currentThread().getName());
            Thread.sleep(200);
            System.out.println("Payment Received");
        }
        catch (Exception e) {
            System.out.println("Cannot Process more than 3 users");
        }
    }
    public static void main(String[] args) {
        try {
            ExecutorService threadPool = Executors.newFixedThreadPool(20); // 20 threads honge
            Thread thread = new Thread(()->{
                try {
                    while(true) {
                        Random random = new Random();
                       try {
                            User user = new User(random.nextInt(1000000)); // ye user bana dega
                            blockingQueue.put(user);
                        }
                       catch (Exception e) {
                           break; // gadbad me nikal jaau loop se
                       }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            for(int i = 0; i < 20; i++) {
                threadPool.submit(()->{
                    while (true) {
                        try {
                            User user = blockingQueue.take();
                            String madeUpUser = "User - " + user.userNumber;
                            sempahore.acquire();
                            try{
                                makePayment(madeUpUser, Thread.currentThread().getName());
                            } finally {
                                sempahore.release();
                            }

                        }
                        catch (Exception e) {

                        }
                    }
                });
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
