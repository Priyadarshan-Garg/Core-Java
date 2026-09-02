package threading;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BGMI_Lobby {
    public static void joinPlayers(Thread thread, String player) {
        try {
            Random random = new Random();
            Thread.sleep(random.nextInt(1, 5) * 1000);

            System.out.println("Player number " + player + " with thread number "+ thread.getName() + " has joined");
        }
        catch (InterruptedException e) {
            e.getMessage();
        }
    }


    // Jaise hi 4 log await() par aayenge, ye apne aap chal jayega!
    private static final CyclicBarrier barrier = new CyclicBarrier(4, () -> {
        System.out.println("Match Started! Aeroplane is taking off! \n");
    });

    public static void main(String[] args) {


        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        int round = 1;

        while (round <= 2) {
            try {
                System.out.println("Round " + round);

                for (int i = 1; i <= 4; i++) {
                    int num = i;
                    // Main thread ye line chalayega and next itr me chala jayega
                    threadPool.submit(() -> {
                        // Player join hua
//                        ek thread niklega pool se and join karega
                        joinPlayers(Thread.currentThread(), Integer.toString(num));

                        try {

                            barrier.await(); // main thread yaha tak pahoch hi nahi pata saare player thread
                            // yaha aakar wait karte hai jaise hi chaaro aaye fir vo runnable chalta hai
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }

                // Main thread ko thodi der sula dete hain taaki agla round turant shuru na ho jaye
                Thread.sleep(6000);
                round++;

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        threadPool.shutdown(); // Kaam khatam hone ke baad pool band kar diya
    }
}