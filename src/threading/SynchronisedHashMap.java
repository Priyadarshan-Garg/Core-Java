package threading;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
      Under the Hood:
    ReentrantReadWriteLock ke andar ek single 32-bit Integer variable hota hai jise State kehte hain
    (yeh AQS - AbstractQueuedSynchronizer ka hissa hai)
    Pehle 16 bits: Batate hain ki kitne Readers andar hain (Reader Counter).
    Aakhri 16 bits: Batata hai ki kya koi Writer andar hai (Writer Flag).

    READLOCK,LOCK()
    JVM check karta hai: "Kya aakhri 16 bits (Writer Flag) mein koi baitha hai?"
    Agar koi Writer nahi hai (Flag = 0), toh JVM us thread ko kabhi block nahi karta!
    JVM bas ek CAS (Compare-And-Swap) operation chalata hai aur pehle 16 bits wale Counter ko +1 kar deta hai.
    Thread seedha method ke andar ghus jata hai.

    Write Lock = Maintenance Sign: Jab painting badalni ho (Write), toh maintenance wala aata hai.
    Wo tab tak wait karega jab tak Counter 0 nahi ho jata (saare readers bahar nahi chale jaate).
    Jaise hi counter 0 hua, wo main gate par board laga deta hai: "Closed for Maintenance".
     Ab na koi naya reader andar aa sakta hai, na koi aur writer.
 */

public class SynchronisedHashMap {

    //     synchronized HashMap<Integer, Integer> map = new HashMap<>(); me ye nahi kar paa rha hu
    static HashMap<Integer, Integer> map = new HashMap<>();

    // DIkhne me method level par lock lag rha hoga but actually me
    // SynchronisedHasmap.class pe lag rha hai. Puri class pe yaa hashmap pe tala
    public static synchronized int getUnsafe(int obj) {
        System.out.print("Get " + Thread.currentThread().getName());
        return map.get(obj);
    }

    public static synchronized void addUnsafe(int obj, int val) {
        System.out.print("Put " + Thread.currentThread().getName());
        map.put(obj, val);
    }

    // asli me hum class ke do hisse kar denge read and write ke liye
    static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
//    static ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    // fairness kar di to writer starvation to nahi hogi but unfair me barging hoti hai
    // jisse thorughput high rehta hai but ab fairness se strict queue order maintain ho gya
    // to sab threads ko wait karna hoga and 100x slow ho gya

    public static Integer getSafe(int obj) {
        // Sirf READ lock lagaya
        lock.readLock().lock();
        try {
            System.out.println(java.time.LocalTime.now() + " -> Get " + Thread.currentThread().getName());
            System.out.println("Get " + Thread.currentThread().getName());
            Thread.sleep(100); // 100ms lag rahe hain padhne me
            return map.get(obj);
        } catch (InterruptedException e) {
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public static void addSafe(int obj, int val) {
        // WRITE lock lagaya (Ye sabko block karega)
        lock.writeLock().lock();
        try {
            System.out.println(java.time.LocalTime.now() + " -> Put " + Thread.currentThread().getName());

            Thread.sleep(100);
            map.put(obj, val);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void main(String[] args) {
//        System.out.println();  --> ye seynchronised hai
        ExecutorService threadPool = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 20; i++) {
            int finalI = i;
            if (i % 5 == 0) { // Har 5th item Writer hai
                threadPool.submit(() -> addSafe(finalI, 99));
            } else { // Baaki sab Readers hain
                threadPool.submit(() -> getSafe(finalI));
            }
        }
        threadPool.shutdown();
    }

}
