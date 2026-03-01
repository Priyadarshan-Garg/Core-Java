package threading;

import java.util.Random;
import java.util.concurrent.*;

// Ek simple class jo order ka data hold karegi
class TradeOrder {
    String company;
    int price;

    TradeOrder(String company, int price) {
        this.company = company;
        this.price = price;
    }
}

public class HighFrequencyTrading {

    // 1. Storage: Sirf aakhiri price yaad rakhne ke liye (Thread-safe)
    private static ConcurrentHashMap<String, Integer> storageBook = new ConcurrentHashMap<>();

    // 2. The Bridge: Producer aur Consumer ke beech ki Queue (Max 1000 ki limit - Backpressure)
    private static BlockingQueue<TradeOrder> orderQueue = new ArrayBlockingQueue<>(1000);

    public static void main(String[] args) {
        System.out.println("📈 Wall Street Market is OPEN!\n");

        // ==========================================
        // 🚀 THE PRODUCER (Market Data Engine)
        // ==========================================
        Thread marketDataProducer = new Thread(() -> {
            Random random = new Random();
            String[] companies = {"Nike", "Apple", "Tesla", "Google"}; // Multiple companies

            while (true) {
                try {
                    String company = companies[random.nextInt(companies.length)];
                    int currentPrice = random.nextInt(80, 150); // 80 se 150 ke beech price

                    TradeOrder order = new TradeOrder(company, currentPrice);

                    // Queue mein daalo. Agar Queue full hai, toh ye line apne aap wait karegi!
                    orderQueue.put(order);

                    Thread.sleep(10); // Har 10 millisecond mein naya price aayega (CPU safe)
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        marketDataProducer.start();

        // ==========================================
        // 🤖 THE CONSUMER (Trading Bot Pool)
        // ==========================================
        // 10 threads ka pool kaafi hai is high speed data ke liye
        ExecutorService tradingBotPool = Executors.newFixedThreadPool(10);

        // Hum 10 bots ko kaam par laga dete hain
        for (int i = 0; i < 10; i++) {
            tradingBotPool.submit(() -> {
                while (true) {
                    try {
                        // Queue se data nikalo. Agar Queue khali hai, toh bot apne aap wait karega!
                        TradeOrder order = orderQueue.take();

                        // 1. Map mein record update karo
                        storageBook.put(order.company, order.price);

                        // 2. Trading Logic (Fast Action)
                        if (order.price < 100) {
                            System.out.println("🚨 BUY SIGNAL! " + order.company + " price dropped to $" + order.price + " (Processed by " + Thread.currentThread().getName() + ")");
                        }

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }
    }
}