package threading;

import java.util.concurrent.CompletableFuture;

public class ProxyBootupModern {
     void main(String[] args) {
        System.out.println("🚀 DispatchedProxy Booting Up (Modern Way)...\n");
        long startTime = System.currentTimeMillis();

        // Teeno tasks ko directly Async workers (ForkJoinPool) ko de diya
        CompletableFuture<Void> serverA = CompletableFuture.runAsync(() -> checkHealth("Server A", 2000));
        CompletableFuture<Void> serverB = CompletableFuture.runAsync(() -> checkHealth("Server B", 3000));
        CompletableFuture<Void> serverC = CompletableFuture.runAsync(() -> checkHealth("Server C", 1000));

        // .allOf() exactly Latch ki tarah kaam karta hai - sabka wait karega
        // .join() yahan pipeline ko block karke final result dega
        CompletableFuture.allOf(serverA, serverB, serverC).join();

        long endTime = System.currentTimeMillis();
        System.out.println("\n✅ All servers healthy! Proxy is now Live.");
        System.out.println("⏱️ Total Boot Time: " + (endTime - startTime) + " ms");
    }

    // Health check method exactly same rahega
    static void checkHealth(String serverName, int delay) {
        try {
            System.out.println("Checking " + serverName + "...");
            Thread.sleep(delay);
            System.out.println(serverName + " is UP!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}