package threading;

import java.util.concurrent.Semaphore;

public class DiningPhilosophers {

    // 5 Forks (Chammach)
    static Semaphore[] forks = new Semaphore[5];

    // TERA LOGIC: Ek Waiter (Bouncer) jo max 4 logon ko table par allow karega
    static Semaphore bouncer = new Semaphore(4);

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            forks[i] = new Semaphore(1); // Har chammach 1 permit
        }

        // 5 Philosophers ko table par bithao
        for (int i = 0; i < 5; i++) {
            final int id = i;
            new Thread(() -> philosopher(id)).start();
        }
    }

    static void philosopher(int id) {
        int leftFork = id;
        int rightFork = (id + 1) % 5;

        try {
            System.out.println("Philosopher " + id + " is thinking...");

            // 1. TERA HACK: Pehle Waiter se permission lo. (Sirf 4 log hi aage badh payenge)
            bouncer.acquire();

            // 2. Chammach uthao
            forks[leftFork].acquire();
            System.out.println("Philosopher " + id + " picked up left fork " + leftFork);

            forks[rightFork].acquire();
            System.out.println("Philosopher " + id + " picked up right fork " + rightFork);

            // EATING (Critical Section) 
            System.out.println("🍽️ Philosopher " + id + " is EATING!");
            Thread.sleep(1000);

            //  PUT FORKS DOWN 
            forks[leftFork].release();
            forks[rightFork].release();
            System.out.println("Philosopher " + id + " put down both forks.");

            // 3. Khana ho gaya, Waiter ko pass wapas de do taaki agla bhookha aa sake
            bouncer.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}