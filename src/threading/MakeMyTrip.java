package threading;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MakeMyTrip {
    static String cabAPI() {
        try {
            Thread.sleep(1000);
            return "Fecting a cab";
        }
        catch (Exception exception) {
            throw  new RuntimeException("Thread Expception in cab");
        }
    }
    public static String flightAPI() {
        try {
            Thread.sleep(3000);
          return "Fetching a flight";
        }
        catch (InterruptedException e) {
            throw  new RuntimeException("Thread Expception in Flight");
        }
    }
    static String hotelAPI() {
        try {
            Thread.sleep(2000);
            return "Fetching a hotel" ;
        }
        catch (InterruptedException e) {
            throw  new RuntimeException("Thread Expception in Hotel");
        }
    }
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        // Thread pool ko future ko kese map karu ki ye thread ye future karega and
        // har future ko kese batau ki ye method call karna hai tuje
        CompletableFuture<String> futureFlight = CompletableFuture.supplyAsync(() -> flightAPI(), threadPool); // kaam de diya and resource bata diya konsa use karna hai
        CompletableFuture<String> futureCab = CompletableFuture.supplyAsync(() -> cabAPI(), threadPool);
        CompletableFuture<String> futureHotel = CompletableFuture.supplyAsync(() -> hotelAPI(), threadPool);
        CompletableFuture<Void> allFuture = CompletableFuture.allOf(futureFlight, futureCab, futureHotel);
        allFuture.join(); // wait karo jab tak sare future kaam kareke naa jaye
        System.out.println(futureFlight.join() + " " +  futureCab.join() + " " + futureHotel.join()); // getSafe try block me ana chah rha
        threadPool.shutdown(); // new thing : ye nahi kiya to application band nahi hua because
        // jvm ko explicitly ye threads jinda rakhne hote hai
    }
}
