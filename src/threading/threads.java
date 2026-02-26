package threading;

public class threads {
    public static void main(String[] args) {
        for (int i = 1; i < 1_000_000; i++) {
//            old school
//            new Thread(()-> {
//                try{
//                   Thread.sleep(10000000);
//                }
//                 catch (Exception exception){
//                    exception.printStackTrace();
//                }
//            }).start();
            // new way
//            Thread.ofVirtual().start(() -> {
//                try {
//                    Thread.sleep(10000000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });

//            System.out.println("Done " + i);





        }
    }
}
