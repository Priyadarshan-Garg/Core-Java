package threading;

public class PingPongIntermediate {
    private static int limit;
    public int count = 1;

    public PingPongIntermediate(int number) {
        limit = number;
    }

    public synchronized void printNumber(int myTurn) {
        while (count <= limit) {
            int rem = count % 3;
            if(rem == myTurn) {
                System.out.println("Printed by : " + Thread.currentThread().getName() + " " + count++);
                notifyAll();
            }
            else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void main(String[] args) {
        PingPongIntermediate pingPongIntermediate = new PingPongIntermediate(25);
        Thread one = new Thread(()->pingPongIntermediate.printNumber(1));
        Thread two = new Thread(()->pingPongIntermediate.printNumber(2));
        Thread three = new Thread(()->pingPongIntermediate.printNumber(0));
        one.setName("one");
        two.setName("two");
        three.setName("three");
        one.start();
        two.start();
        three.start();
        System.out.println(Thread.currentThread().getName());
    }

}
