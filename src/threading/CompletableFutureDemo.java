package threading;



import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureDemo {

    public static String fetchProfile() {
        try {
            Thread.sleep(3000);
            boolean isServerDown = true;
            if (isServerDown) {
                throw new RuntimeException("Bhayankar Error: Profile API ka server udd gaya!"); // means ye nahi
                // chal rha agar me THrowable::string karta to aa jati
            }
            return "Profile{name=Priyo}";
        } catch (InterruptedException e) {

            throw new RuntimeException();
        }
    }


    public static String fetchStats() {
        try {
            Thread.sleep(2000);
            return "Stats{rank=1}";
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        ExecutorService myService = Executors.newFixedThreadPool(20);
        CompletableFuture futureProfile = CompletableFuture.supplyAsync(() -> fetchProfile(), myService)
                .exceptionally(ex-> "Problem aa gyi");
        CompletableFuture futureStats = CompletableFuture.supplyAsync(() -> fetchStats(), myService).exceptionally((Throwable::toString));
        System.out.println("Main Thread is free other doing their work");
        futureProfile.thenCombine(futureStats, (res1, res2) -> res1 + " " + res2).thenAccept((finalResult) -> {
            System.out.println(finalResult);
        });
        // as we can see main thread free hote hai in sab daemon threads maar deta hai so
        Thread.sleep(4000);
        System.out.println("Last statement");
        myService.shutdownNow();
    }
}
