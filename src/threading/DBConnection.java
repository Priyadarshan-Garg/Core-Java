package threading;

class DBConnection {
    // 🚨 BINA IS KEYWORD KE YE PATTERN FAIL HAI (Neeche batata hu kyu)
    // cpu ffast ke chakkar me steps reorder kar det hai jese
    // step 1 give it a new address
    // step  3 assign new address to the object
    // step 2 call constructor -> ye jada time leta hai
    // isliye volatile se hum ye kehte hai ki bhai step by step chalo hoshiyar mat dikhao
    private static volatile DBConnection instance = null;

    private DBConnection() {
        // Heavy initialization
    }
    // ek dum galat
    public static DBConnection getInstanceUnsafe() {
        if (instance == null) {
            instance = new DBConnection(); // 🚨 THE DANGER ZONE
        }
        return instance;
    }
    public static DBConnection getInstance() {
        // Check 1: PERFORMANCE ke liye (Bina lock ke)
        if (instance == null) { 
            
            // Yahan sirf pehli baar aane wale threads phasege
            synchronized (DBConnection.class) {
                
                // Check 2: SAFETY ke liye (Lock ke andar)
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance; // Lock se bahar, full speed!
    }


    // mordern way using jvm interanals yaad kar <clinit>
    private static class Holder {
        // Ye object tab tak nahi banega jab tak Holder class load nahi hoti
        private static final DBConnection INSTANCE = new DBConnection();
    }

    public static DBConnection getInstanceModernWay() {
        return Holder.INSTANCE; // Bina lock ke seedha return!
    }
}