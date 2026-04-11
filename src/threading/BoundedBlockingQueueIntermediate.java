//package threading;
//
//import java.util.LinkedList;
//import java.util.Optional;
//import java.util.Queue;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.locks.ReentrantLock;
//
//class Node<T> {
//    Node<T> head;
//    Node<T> tail;
//}
//public class BoundedBlockingQueueIntermediate{
//
//    /*
//    1. Problem Kya Hai? (The Core Concept)
//    Humein ek aisi Queue banani hai jiski capacity fixed ho (manlo 5 items).
//    Producer: Agar queue full hai, toh producer wait karega jab tak jagah na bane.
//    Consumer: Agar queue empty hai, toh consumer wait karega jab tak koi item na aa jaye.
//    Thread Safety: Multiple threads ek saath data corrupt na karein, isliye hum locks use karte hain.
//
//    Phase 1:
//    Bottleneck: Kyunki lock ek hi tha, toh agar koi Producer item daal raha hai, toh Consumer item nikal nahi sakta tha.
//    Woh dono ek dusre ko block kar rahe the. High-performance systems (jaise Load Balancers) mein humein itna delay nahi chahiye.
//
//    Phase 2: Basic wala
//    putLock: Sirf Producers ke liye.
//    takeLock: Sirf Consumers ke liye.
//    AtomicInteger count: Kyunki do alag locks hain, toh humein ek thread-safe tarika chahiye tha size track karne ke liye,
//    taaki dono locks ke threads bina ek-dusre ko block kiye count dekh sakein.
//     */
//
//    private final AtomicInteger size = new AtomicInteger();
//    private final ReentrantLock takeLock = new ReentrantLock();
//    private final ReentrantLock putLock = new ReentrantLock();
//
//    private Queue<Node> queue = new LinkedList<>();
//
//    public int size() {
//        return size.get();
//    }
//
//    public void enque(T element) {
//        try {
//            putLock.lock(); // me put kar rha hu koi dusra put karne nahi aayega
//            while (size.get() >= queue.size()) {
//                takeLock.notify(); // mtlb consumer ko kaho isme se lele
//            }
//            queue.offer(element);
//            size.incrementAndGet();
//        } catch (Exception e) {
//
//        } finally {
//            putLock.unlock();
//        }
//
//    }
//
//    public T deque() {
//        T element = null;
//        try {
//            takeLock.lock();
//            while (queue.isEmpty()) {
//                putLock.notify(); // mtlb consumer ko kaho isse bhar de
//            }
//            element = queue.remove();
//            size.decrementAndGet();
//
//        } catch (Exception e) {
//
//        } finally {
//            takeLock.unlock();
//        }
//        return element;
//    }
//}
