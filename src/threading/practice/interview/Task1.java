package threading.practice.interview;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Task1 {
    static int cnt = 0;

    static synchronized void inc() {
        cnt++;
    }

    public static void main(String[] args) {

    }
}
