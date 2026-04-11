package threading;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBlockingQueueBasic<T> {

    private final int capacity;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    private final Queue<T> queue = new LinkedList<>();
    public BoundedBlockingQueueBasic(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be > 0");
        this.capacity = capacity;
    }
    public void enque(T element) {
        lock.lock();
        try {
            while(queue.size() == capacity) {
                notFull.await(); // bhai full hai me lock kholke waiting room me jaa rha hu
            }
            queue.add(element);
            notEmpty.signal(); // mene daal diya ab consumer aake le ja sakta hai
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            lock.unlock();
        }
    }

    public  T deque () {
       lock.lock();
        try{
            while (queue.isEmpty()) {
                notEmpty.await(); // khaali hai to consumer notEmpty ke waiting room me
            }
            T element = queue.remove();
            notFull.signal(); // ab ye khaali to producer isme daal de
            return element;
        }
      catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public int size() {
       lock.lock();
       try {
           return queue.size();
       }
       finally {
           lock.unlock();
       }
    }
}
