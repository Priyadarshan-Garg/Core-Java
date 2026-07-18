package threading;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    static int id;
    int amount;
    // Never Do this
    // 2 user A and B dono ek saath ek dusre ko transfer kar rhe hai
    public void transfer(Account target, int amount) {
        // THread 1 aaya locked account A
        synchronized (this) {
            // context switch then Thread 2 comes try accquire lock of above to B pe lock laga diya
            // Again context switch Thread 1 ab B ko lock lagana chahta hai but B to Thread 2 new accquire kar diya
            // Sam Thread 1 ne A ko jo ki THread 2 ko chahiye tha
            synchronized (target) {

                this.amount -= amount;
                target.amount += amount;
            }
        }
    }

    // use this
    public void industryStandardTransfer(Account target, int amount) {
        Account firstLock = this.id < target.id ? this : target;
        Account secondLock = this.id > target.id ? this : target;
        // hamesha choda accound hi pehle lock lega koi bhi thread kyu na ho
        synchronized (firstLock) {
            synchronized (secondLock) {
                // taam jhaam
            }
        }
        /*
        ye actually sequntial hai but iss level pe ye fine grained lock hai
        yaha multithreading nahi ho rhi but ese multiple accounts transfer kar rhe hai
        to bank yaa large level pe to parallelism hai
         */
    }
    // But cpu overlaod ki waja se ek thread schedule band kar de to
    // Infinite blocking / Thread starvation ho jayega use this
    public void optimisticTransfer(Account target, int amount) throws InterruptedException {
        Lock lockA = new ReentrantLock();

        Lock lockB = new ReentrantLock();

// Thread 2 darwaze par aata hai aur bolta hai:
// "Main max 3 seconds wait karunga, lock mila toh theek, warna bhaad me jaye!"
        if (lockA.tryLock(3, TimeUnit.SECONDS)) {
            try {
                if (lockB.tryLock(2, TimeUnit.SECONDS)) {
                    try {
                        // Paise transfer karo
                    } finally {
                        lockB.unlock(); // Always in finally
                    }
                } else {
                    System.out.println("Transaction Failed: threading.Account B busy hai.");
                }
            } finally {
                lockA.unlock();
            }
        } else {
            System.out.println("Transaction Failed: threading.Account A hold par hai (System busy).");
        }
    }


}
