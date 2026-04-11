package threading;



public class PingPong {

    private static int maxLimit;
    private static int count = 1;
    public static void setMaxLimit(int x) {
        maxLimit = x;
    }

    public synchronized void printOdd() {
        while (count <= maxLimit) {
            if(count % 2 == 0) {
                try {
                    wait(); // I can't do anything so I'm gonna sleep
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                System.out.println("Odd Thread: " + count);
                count++;
                notify(); // I have done my work so notify other thread
            }
        }
    }

    public synchronized void printEven() {
        while (count <= maxLimit) {
            if(count % 2 == 1) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                System.out.println("Odd Thread: " + count);
                count++;
                notify(); // Agar 3 yaa usse jada threads hue to os kisi ek ko uthayega jisse deadlock ho sakta hai
                /*
                Maan le T1 ne 1 print kiya aur notify() kiya. T2 ko uthna chahiye tha (kyunki uski baari hai),
                par galti se T3 uth gaya! T3 dekhega ki "Are count toh 2 hai, meri baari nahi hai", aur woh wapas so jayega.
                Ab saare threads sote reh jayenge aur tera program fass jayega!
                 */
            }
        }
    }

    public  void main(String[] args) {
        setMaxLimit(10);
        Thread odd = new Thread(()-> printOdd());
        Thread even = new Thread(()-> printEven());
        odd.start();
        even.start();

    }
}
