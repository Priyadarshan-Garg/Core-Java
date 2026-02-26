package threading;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NewsSystem {
    public  final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);
    // fairness true mtlb queue me abhi reader hai ? ek specific time ke baad reader  ruk jaa writer aa ja but baar OS check karta hai konsa thread hai
    String news =  "Purani News";

    void readNews() {
        reentrantReadWriteLock.readLock().lock();
        try {
            System.out.println("I'm reading news");
        }
        finally {
            reentrantReadWriteLock.readLock().unlock();
        }
    }

    void updateNews(String newNews) {
        reentrantReadWriteLock.writeLock().lock(); // automatic check karta hai ki koi thead lock karke to nahi betha

        try {
            this.news = newNews;
        }
        finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
    }
    void updateNewsUsingLockDowngrading(String newNews) {
        reentrantReadWriteLock.writeLock().lock();;
        try {
            this.news = newNews;
            reentrantReadWriteLock.readLock().lock(); // down grading start
        }
        finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        try {
            System.out.println("This is updated news : " + news);

        }
        finally {
            reentrantReadWriteLock.readLock().unlock();
        }
    }
}
