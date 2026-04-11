package threading;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TwoLockBoundedQueue<T> {

        private final ReentrantLock takeLock = new ReentrantLock();
        private final ReentrantLock putLock = new ReentrantLock();
        private final Condition notFull = takeLock.newCondition();
        private final Condition notEmpty = putLock.newCondition();
        private final AtomicInteger capacity = new AtomicInteger();
        private final Queue<T> queue = new LinkedBlockingQueue<>();
        public TwoLockBoundedQueue(int capacity) {
            this.capacity.set(capacity);
        }

        public void enque(T element) {
           putLock.lock();
           try {
               while(capacity.get() == queue.size()) {
                   if(takeLock.isLocked()) {
                       takeLock.unlock();
                   }
                   notFull.await();
               }
               queue.add(element);
           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }
           finally {
               putLock.unlock();
               notEmpty.signal();
           }
        }

        public T deque() {
            takeLock.lock();
            try {
                while (queue.isEmpty()) {
                    if(putLock.isLocked())  {
                        putLock.unlock();
                    }
                    notEmpty.await();
                }
                T element = queue.remove();
                return element;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            finally {
                takeLock.unlock();
                notFull.signal();
            }
        }
        public int size() {
            return capacity.get();
        }
}
