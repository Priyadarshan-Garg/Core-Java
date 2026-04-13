package threading;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool<T> {

    class WokerThread extends Thread {
        @Override
        public void run() {
            while (true) {
                // Chalta reh
                try {
                    Runnable task = queue.take();
                    task.run();
                } catch (InterruptedException e) {
                    return; // nikal jao kaam ho gya
                }
            }
        }
    }

    /*
     khud ka thread pool bana jo fixed threads banaye and jinda rakhe and runnable task accept kare withc
     help of queue to process everything in order
     */

    private final LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(); // ye line static karta hu to ye T problem karta hai kyu ?
    ArrayList<WokerThread> list = new ArrayList<>();

    public ThreadPool(int size) {
        for (int i = 0; i < size; i++) {
            list.add(new WokerThread());
            list.get(i).start();
        }
    }

    public void shutDown() {
        for (WokerThread thread : list) {
            thread.interrupt();
        }
    }

    public void submit(Runnable task) {
        try {
            queue.put(task);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ThreadPool<Runnable> threadPool = new ThreadPool<>(5);
        for (int i = 0; i < 5; i++) {
            threadPool.submit(() -> System.out.println("Printed by : " + Thread.currentThread().getName()));
        }

        threadPool.shutDown();
    }

}
