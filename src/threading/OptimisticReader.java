package threading;

import java.util.concurrent.locks.StampedLock;

public class OptimisticReader {
    public final StampedLock sl = new StampedLock();
    int x,y;
    public void read() {
        long stamp = sl.tryOptimisticRead();
        int currX = x;
        int currY = y;
        // koi aya darwaze se ?
        if(!sl.validate(stamp)) {
            stamp = sl.readLock();
            try {
                currX  = x;
                currY =  y;
            }
            finally {
               sl.unlockRead(stamp); //vahi long stamp leta hai jo lock karte waqt deta hai
            }

        }
    }
}
