package threading;

public class ThreadLifeCycle {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(()-> {
            try {
                Thread.sleep(2000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("Just Created : " + thread.getState());
        thread.start();
        System.out.println("After Start : " + thread.getState());

        Thread.sleep(500);
        System.out.println("While Sleeping " +  thread.getState());

        thread.join(); // main thread yaha aake block ho jayega jab tak worker thread apna kaam nahi kar le tab tak
        System.out.println("After join (Finished) : " +thread.getState());
    }
}
