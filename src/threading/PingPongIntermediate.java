package threading;

public class PingPongIntermediate {
    private static int limit;
    public int count = 0;

    public PingPongIntermediate(int number) {
        limit = number;
    }

    public synchronized void printNumber() {
        while (count <= limit) {
            int rem = count % 3;
            if (rem == 0) {
                if (Thread.currentThread().getName().equals("three")) {
                    System.out.println("Printed by : " + Thread.currentThread().getName() + " " + count++);
                    notifyAll();
                } else {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (rem == 1) {
                if (Thread.currentThread().getName().equals("one")) {
                    System.out.println("Printed by : " + Thread.currentThread().getName() + " " + count++);
                    notifyAll();
                } else {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                if (Thread.currentThread().getName().equals("two")) {
                    System.out.println("Printed by : " + Thread.currentThread().getName() + " " + count++);
                    notifyAll();
                } else {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }
    }

    public static void main(String[] args) {
        PingPongIntermediate pingPongIntermediate = new PingPongIntermediate(25);
        Thread one = new Thread(pingPongIntermediate::printNumber);
        Thread two = new Thread(pingPongIntermediate::printNumber);
        Thread three = new Thread(pingPongIntermediate::printNumber);
        one.setName("one");
        two.setName("two");
        three.setName("three");
        one.start();
        two.start();
        three.start();
        System.out.println(Thread.currentThread().getName());
    }

}
