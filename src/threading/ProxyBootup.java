package threading;

import java.util.concurrent.CountDownLatch;

public class ProxyBootup {
    public static  final CountDownLatch downLatch = new CountDownLatch(3);
    public static void main(String[] args) throws InterruptedException {
        System.out.println("🚀 DispatchedProxy Booting Up...\n");

        Thread serverA = new Thread(() -> checkHealth("Server A", 2000)); // Takes 2 sec
        Thread serverB = new Thread(() -> checkHealth("Server B", 3000)); // Takes 3 sec
        Thread serverC = new Thread(() -> checkHealth("Server C", 1000)); // Takes 1 sec

        long startTime = System.currentTimeMillis();

        //  BUGGY BOOTUP SEQUENCE
        serverA.start();
//        serverA.join();

        serverB.start();
//        serverB.join();

        serverC.start();
//        serverC.join();
        downLatch.await();

        long endTime = System.currentTimeMillis();
        System.out.println("\n✅ All servers healthy! Proxy is now Live.");
        System.out.println("⏱️ Total Boot Time: " + (endTime - startTime) + " ms");
    }

    static void checkHealth(String serverName, int delay) {
        try {
            System.out.println("Checking " + serverName + "...");
            Thread.sleep(delay);
            System.out.println(serverName + " is UP!");
            downLatch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}