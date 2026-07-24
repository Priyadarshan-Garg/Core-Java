## 1. How does HashSet know that the second 10 is already present?

```java
    HashSet<Integer> set = new HashSet<>();
    
    set.

add(10);
    set.

add(10);
    set.

add(20);
    
    System.out.

println(set.size());
```

HashSet is basically a thin wrapper around HashMap.

```java
    set.add(10);
```

becomes roughly

```java
    HashMap.put(10,PRESENT);
```

```java
    obj1.hashCode() ==obj2.

hashCode()
```

is possible so java Checks hashCode(). If bucket differs: **Not present**. If bucket is same: java calls **equals()** to
determine whether the objects are actually equal.

---

## 2. What prints ?

```java
class Student {

    int id;

    Student(int id) {
        this.id = id;
    }

    public static void main(String[] args) {
        HashSet<Student> set = new HashSet<>();

        set.add(new Student(1));
        set.add(new Student(1));

        System.out.println(set.size());
    }
}
```

size is **2**
Memory

```text
    s1 ---> Student(id=1)
    
    s2 ---> Student(id=1)
```

You may think Same fields ⇒ same hashCode but that's not true.

--- 

## 3. What happens ?

```java
class Student {

    int id;

    Student(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        return this.id == ((Student) o).id;
    }
}
```

Now overriding **equals** so when we do

```java
    Student s1 = new Student(1);
Student s2 = new Student(1);
    s1.

equals(s2)
```

This returns true because Id match
Now If we do

```java
    HashSet<Student> set = new HashSet<>();
    
    set.

add(s1);
    set.

add(s2);
```

equals() returns true, so only one object should exist. Not necessarily.

```java
    s1.hashCode() =123
        s2.

hashCode() =987
```

So they are treated as different objects and assigned to different buckets.

```text
    If two objects are equal according to equals(), they MUST return the same hashCode().
```

---

## 4. What will it print ? ssume equals() and hashCode() are based on id.

```java
    HashMap<Student, String> map = new HashMap<>();

Student s = new Student(1);
    
    map.

put(s, "PD");

s.id =2;

        System.out.

println(map.get(s));
```

Assume **hashCode() = id**
HashMap Stores

```java
 bucket(1)

Student(1) ->"PD"
```

when doing s.id = 2 You did not reinsert the object. You only mutated it.
When you do **map.get(s)** It searches the bucket not entire table and bucket 2 is empty so it returns **null**.

---

## 5. Whats the output ? Error ? prints all value ?

```java
    List<Integer> list = new ArrayList<>();
    
    list.

add(1);
    list.

add(2);
    list.

add(3);
    
    for(
Integer x :list){
        if(x ==2){
        list.

remove(x);
        }
                }

                System.out.

println(list);
```

So the enhanced for loop roughly becomes

```java
    Iterator<Integer> it = list.iterator();
    
    while(it.

hasNext()){
Integer x = it.next();
        ...
                }
```

Arraylist maintains a modCount as internal count so when we

```java
    add()

remove()

clear()
```

java increments it. And the iterator stores **expectedModCount** During it checks

```java
    expectedModCount ==modCount
```

Now they are not it equal and when these methods are called **it.next** it throws  **ConcurrentModificationException**.

---

## 6. Which is best and suitable way to remove even numbers from this list ?

```java
    List<Integer> list = List.of(1, 2, 3, 4, 5);
```

One way is

```java
    List<Integer> result = new ArrayList<>();
    
    for(
Integer x :list){
        if(x %2!=0){
        result.

add(x);
        }
                }
list =result;
```

**Follow up** :How do you remove while iterating?

```java
    list.removeIf(x ->x %2==0);
```

---

## 7. What prints ?

```java
    try{
        return;
        }
        finally{
        System.out.println("finally");
    }
```

It will print **"finally"**.

---

## 8. What returns ?

```java
    try{
        return 10;
        }
        finally{
        return 20;
        }
```

When we do **return 10** it does not leaves method immediately. JVM roughly does

```java
    result =10;
```

and before returning result finally executes. The finally block doesn't just run. It overrides the pending return.
now it becomes **result = 20**.

---

## 9. What will it return ?

```java
public static int test() {

    int x = 10;

    try {
        return x;
    } finally {
        x = 20;
    }
}
```

You will think this. But it's incorrect.

```text
    return x
        ↓
    finally runs
        ↓
    x becomes 20
        ↓
    return 20
```

What actually happens

```text
    return x; // stores
        ⬇️
    temp = 10;
        ⬇️
    finally // executes
        ⬇️
    x = 20;
```

here x changes but not temp which will gets returned by the method.

---

## 10. What gets returned here ?

```java
public static List<Integer> test() {

    List<Integer> list = new ArrayList<>();

    list.add(10);

    try {
        return list;
    } finally {
        list.add(20);
    }
}
```

It will return

```text
    [10, 20]
```

Java is **always pass-by-value**.

```text
    temp = reference_to_list;
```

The reference itself is copied by value. So both variables point to the same object. Same object. So modifying through
list changes what temp sees.

---

## 11. Is it thread safe ?

```java
    volatile int count = 0;
count++;
```

volatile guarantees _visibility_, not **atomicity**. The operation count++ consists of read, modify, and write steps. Two
threads can read the same value simultaneously and overwrite each other's updates. Therefore lost updates can still
occur even when the variable is volatile.

---

## 12. Is this thread-safe? What problem can occur? How would you fix it?
```java
public class Singleton {

    private static Singleton instance;

    public static Singleton getInstance() {

        if (instance == null) {
            instance = new Singleton();
        }

        return instance;
    }
}
```
So we can end up creating more than one instance and singleton is broken.
```text
    Thread A 
       ⬇️
    if(condition) // enters and context switch
        ⬇️
    Thread B 
        ⬇️
    if(condition) // enters and context switch
        ⬇️
    Now both are in if statement and will create 2 instances.
```
Classic fix : Use Synchronised method. But it will cost performance. Because even getting instance causes thread overhead.
Double Check locking:
```java
public void getInstance() { // void is just for compilation it does 
    // not depict original motive
    if(instance == null) {

        synchronized(Singleton.class) {

            if(instance == null) {
                instance = new Singleton();
            }

        }
    }

    return instance;
}
```
First check avoids locking most of the time. Once the singleton exists and threads skip synchronization completely.Fast path.
Second check Because two threads could reach the first check simultaneously.
We write .
```java
    private static volatile Singleton instance;
```
Because JVM can reorganise the byte instruction so we are forcing it to be sequential.
Before
```text
1. Allocate memory
2. Construct object
3. Assign reference to instance
```
After 
```text
1. Allocate memory
2. Assign reference to instance
3. Run constructor
```
There is another way also using static variables and blocks but I'll cover in advance problems and explain it later.

---
## 13. Why this is allowed ? Why can't wwe save primitives
```java
    List<String> list = new ArrayList<>();
```
but not
```java
    List<int> list = new ArrayList<>();
```
Because when we do
```java
    ArrayList<Integer> list = new ArrayList<>();
```
Internally JVM does something roughly like this
```java
    Object[] elementData;
```
When you do **list.add(10)** JVM performs _autoboxing_ **Integer.valueOf(10)**
Roughly Memory looks like this 
```text
    Stack
    -----
    list ─────┐
              ▼
    
    Heap
    ----
    ArrayList
       │
       ▼
    Object[] ----> Integer(10)
```

---

## 14. How many total number of objects are in the heap ?
```java
    ArrayList<Integer> list = new ArrayList<>();
    
    list.add(100);
    list.add(200);
```
There are **4 Objects** in the heap. They are :
* ArrayList
* Internal **Object[]** array backing arraylist
* Integer.valueOf(100) **autoboxing**
* Integer.valueOf(200) **autoboxing**

This is how it looks in memory
```text
            Stack
            -----
            list
              |
              v
            
            Heap
            -----
            ArrayList
               |
               v
            Object[]
             |    |
             v    v
            100  200
```
---

## 15. What is the output of last line ?
```java
    Integer a = 100;
    Integer b = 100;
    System.out.println(a == b);
    a++;
    System.out.println(a == b);
```
The answer is **false**. Integer(100) does not becomes Integer(101). Instead Java roughly does: 
```java
    a = Integer.valueOf(a.intValue() + 1);
```
Now memory looks like this 
```text
    b ─────► Integer(100)
    
    a ─────► Integer(101)
```
