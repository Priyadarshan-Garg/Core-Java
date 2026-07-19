package systemDesign;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
/*
    Tujhe ek API banani hai jo check kare ki ek user ne last 1 minute mein kitne calls maare.
    Agar 100 calls se zyada hain, toh error de do.
    Challenge:
    Multiple threads ek hi user ke liye request bhej rahe hain.
    Map update karte waqt race condition kaise rokega bina poore map ko lock kiye?
 */

class Request {
    public Request(int cnt, LocalDateTime time) {
        this.cnt = cnt;
        this.time = time;
    }

    int cnt;
    LocalDateTime time;

}
public class MemoryRateLimiter {

    private static ConcurrentHashMap<Integer, AtomicInteger> map = new ConcurrentHashMap<>();

//    private static void unsafeAPI(int id) {
//        // do thread aaye dono ne primitve int ko atomic nahi samjha
//        while (map.containsKey(id) && map.get(id).cnt > 100 && LocalDateTime.now() - map.get(id).time < 1) {
//            System.out.println("Hold your horses, you have been rate limited");
//        }
//        map.put(id, new Request(map.get(id).cnt + 1, LocalDateTime.now()));
//    }
    public static void safeAPI(int id) {
        AtomicInteger count = map.computeIfAbsent(id, k -> new AtomicInteger(0));
        int currentCnt = count.getAndIncrement();
        if (currentCnt > 100) {
            System.out.println("Hold your horses, User " + id + " limit exceeded!");
        } else {
            System.out.println("Request allowed for User " + id + ". Count: " + currentCnt);
        }
    }
    public static void main(String[] args) {
        ExecutorService threadPool = null;
       try {
           threadPool = Executors.newFixedThreadPool(10);

           for(int i = 0; i < 1000; i++) {
               threadPool.submit(()-> safeAPI(0));
           }
       }
        finally {

        threadPool.shutdown();
        }
    }
}
