//package threading;
//
//import java.util.ArrayList;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//
//public class ThreadPool<T> {
//    public volatile boolean isShutdown = false;
//    class WokerThread extends Thread {
//        @Override
//        public void run() {
//            while (true) {
//                if(isShutdown && queue.isEmpty()) {
//                    return;
//                }
//                // Chalta reh
//                try {
//                    Runnable task = queue.take();
//                    task.run();
//                } catch (InterruptedException e) {
//                }
//            }
//        }
//    }
//
//    /*
//     khud ka thread pool bana jo fixed threads banaye and jinda rakhe and runnable task accept kare withc
//     help of queue to process everything in order
//     */
//
//    private final LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(); // ye line static karta hu to ye T problem karta hai kyu ?
//    ArrayList<WokerThread> list = new ArrayList<>();
//
//    public ThreadPool(int size) {
//        for (int i = 0; i < size; i++) {
//            list.addSafe(new WokerThread());
//            list.getSafe(i).start();
//        }
//    }
//
//    public void shutDownNow() {
//        for (WokerThread thread : list) {
//            thread.interrupt();
//        }
//    }
//    public void shutDown() {
//        isShutdown = true;
//        for (WokerThread thread : list) {
//            thread.interrupt();
//        }
//    }
//
//    public void submit(Runnable task) {
//        if (isShutdown) {
//            System.out.println("Pool is shutting down. Task rejected.");
//            return;
//        }
//        try {
//            queue.put(task);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static void main(String[] args) {
//        ThreadPool<Runnable> threadPool = new ThreadPool<>(5);
//        for (int i = 0; i < 5; i++) {
//            threadPool.submit(() -> System.out.println("Printed by : " + Thread.currentThread().getName()));
//        }
//        threadPool.shutDown(); // polite shutdown
//    }
//
//}

package threading;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {

    // 2 Flags (Boss ke 2 alag-alag messages)
    private volatile boolean isShutdown = false;       // Polite Shutdown flag
    private volatile boolean isShutdownNow = false;    // Hard Shutdown flag

    private final LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    private final ArrayList<WorkerThread> list = new ArrayList<>();

    // MAZDOOR KA LOGIC (Worker Thread)
    class WorkerThread extends Thread {
        @Override
        public void run() {
            while (true) {
                // 🚨 CONDITION 1 (Aag lag gayi): Turant ghar bhago!
                if (isShutdownNow) {
                    System.out.println(Thread.currentThread().getName() + " -> Hard Shutdown, bhaago!");
                    return;
                }

                // 🚪 CONDITION 2 (Polite Shutdown): Dukan band hai AUR saara kaam khatam ho chuka hai
                if (isShutdown && queue.isEmpty()) {
                    System.out.println(Thread.currentThread().getName() + " -> Queue khali hai, izzat se ghar ja raha hu.");
                    return;
                }

                try {
                    // Kaam uthao aur karo
                    Runnable task = queue.take();
                    task.run();
                } catch (InterruptedException e) {
                    // Boss ne interrupt karke jagaya!
                    // Yahan se 'return' NAHI karenge.
                    // Loop wapas ghumega aur upar wali 2 Conditions (if checks) khud decide karengi kya karna hai.
                }
            }
        }
    }

    public ThreadPool(int size) {
        for (int i = 0; i < size; i++) {
            list.add(new WorkerThread());
            list.get(i).start();
        }
    }

    public void submit(Runnable task) {
        if (isShutdown || isShutdownNow) {
            System.out.println("Factory is closed. Cannot accept new tasks!");
            return;
        }
        try {
            queue.put(task);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // THE TWO SHUTDOWNS 👇

    public void shutDown() {
        isShutdown = true; // Board pe likho "Polite Exit"
        for (WorkerThread thread : list) {
            thread.interrupt(); // Sote hue mazdooron ko jagao
        }
    }

    public void shutDownNow() {
        isShutdownNow = true; // Board pe likho "Aag Lag Gayi!"
        for (WorkerThread thread : list) {
            thread.interrupt(); // Sote hue mazdooron ko jagao
        }
    }
}

/*
 Key Observations
    ^^ Treads ki kabhi hatya (murder) nahi hoti, unhe sirf request (interrupt) kiya jata hai ki bhaisaab apna loop
     todo aur gracefully khud return karke exit ho jao. ^^
    Jaise hi thread apne run() method se bahar aata hai (ya return hit karta hai),
    us thread ka logic officially khatam ho jata hai. JVM us thread ki state ko
    RUNNABLE se change karke TERMINATED (Dead) kar deta hai.

    Thread actual mein tere Operating System (Linux/Windows) ka resource hota hai.
    Jaise hi run() method khatam hota hai, JVM turant OS ko signal deta hai: "Bhai,
    is OS-thread ka kaam ho gaya." OS milliseconds ke andar us thread ki
    Stack Memory (jahan uske local variables the) aur underlying native thread
    ko destroy kar deta hai. Isme 60 seconds ka koi wait nahi hota.

    Ab Heap memory mein sirf ek khali khoka bacha hai—tera WorkerThread ka
    Java object (new WorkerThread()). Kyunki wo thread mar chuka hai, agli baar
    jab bhi JVM ka Garbage Collector (GC) chalega, wo dekhega ki is object ka
    koi kaam nahi hai aur wo usko RAM se puri tarah uda dega
 */