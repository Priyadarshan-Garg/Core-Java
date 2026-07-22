## Q1 Difference between: ArrayList LinkedList
* Internally kaise store hote hain?
* Random access (get(i)) kis mein fast hai aur kyu?
* Middle mein insert/delete kis mein better hai aur kyu?
* Real life mein tum kab ArrayList aur kab LinkedList choose karoge?
```text
ArrayList internally dynamic array use karti hai,
isliye random access O(1) hota hai. LinkedList nodes ke through
connected hoti hai, isliye kisi index tak pahunchne ke liye
traversal O(N) lagta hai. LinkedList mein insertion/deletion O(1) 
ho sakta hai if we already have the node reference,
otherwise traversal cost add ho jati hai. 
Most application code mein ArrayList preferred hoti hai due to better cache locality and faster reads.
```
## How does ArrayList resize internally? What is Dynamic Array? What is Cache Locality?
```text
ArrayList is implemented using a dynamic array.
When the internal array becomes full, Java allocates a larger array and copies existing elements into the new array.
This allows logical resizing while maintaining O(1) average append performance.
ArrayList stores elements in contiguous memory locations.
CPU fetches nearby memory into cache lines.
When accessing:

list.get(i)

the next elements are usually already in cache.
LinkedList nodes are scattered across memory.
CPU must repeatedly follow references, causing cache misses.
Because of this, ArrayList is often faster than LinkedList even when theoretical complexities look similar.
```

## Why is add() amortized O(1)?
```text
Capacity full
↓
Allocate new array
↓
Copy N elements
↓
Insert new element

Ye operation:O(N) ata hai.
```

## What happens during resize?
```text
Creates larger AL and copies elements.
Old array becomes unreachable
↓
Garbage Collector decides later
↓
Memory reclaimed
```
## If copying takes O(N), why do we still say average insertion is O(1)?
```text
Suppose: Capacity = 10
11th insertion:Create bigger array -> Copy 10 elements Insert -> Expensive.
```

## String s1 = "hello"; String s2 = "hello";

```text
String literals JVM String Pool mein jaate hain.
JVM pehle check karta hai:
hello already exists?
Agar haan:
Reuse existing object
```

## What is string pool
```text
String Pool is a special area managed by JVM
where string literals are stored.
If the same literal already exists,
JVM reuses the existing object instead
of creating a new one.
```

### String Pool

String literals are stored in JVM String Pool.

String s1 = "hello";
String s2 = "hello";

s1 == s2  // true

because both reference the same pooled object.

---

String s3 = new String("hello");

s1 == s3  // false

because new creates a separate heap object.

---

intern()

Returns the pooled version of the string.

s1 == s3.intern() // true


## Thread Safe ?
 ```java
Map<String, Integer> map = new HashMap<>();
Thread t1 = new Thread(() -> map.put("A", 1));
Thread t2 = new Thread(() -> map.put("B", 2));
```
```text
Race Conditions
Memory Visibility Issues
Data Corruption
```
## HashMap
- Not thread-safe
- No synchronization
- Multiple threads modifying it can cause race conditions
## ConcurrentHashMap

- Thread-safe
- Supports concurrent reads/writes
- Does not lock the entire map
- Uses fine-grained synchronization and CAS

## Common misconception
Not thread-safe != Exception
Most concurrency bugs silently produce wrong behavior.
## count++
count++ is NOT atomic.
Internally:

1. Read
2. Increment
3. Write

Multiple threads can interleave these operations.
Result:
Race Condition
Lost Updates

Solutions:
- synchronized
- AtomicInteger
- Locks

## Volatile JVM ko bolta hai:
Is variable ko cache mat samjho.
Latest value memory se padho.
Updates dusre threads ko visible honi chahiye.

## volatile

Purpose:
Memory Visibility

Without volatile:
One thread may not see updates made by another thread.

With volatile:
Reads and writes are visible across threads.

volatile guarantees:
✅ Visibility
volatile does NOT guarantee:
❌ Atomicity
❌ Thread Safety
Example:
volatile int count;
count++; // still unsafe

```text
1. HashMap computes hashCode() of key.

2. Hash value determines bucket index.

3. Bucket is a slot in an internal array.

4. If bucket is empty:
   insert entry.

5. If bucket already contains entries:
   compare keys using equals().

6. If key matches:
   update value.

7. Otherwise:
   collision occurs and entry is added.

8. In Java 8, long collision chains may become
   Red Black Trees for better performance.
   ```


## Why override equals and hashCode together?
```text
Hash-based collections such as HashMap and HashSet use hashCode() to find the bucket and equals() to determine logical equality.
If equals() is overridden without hashCode(), equal objects may end up in different buckets and HashMap behavior becomes
incorrect. Therefore both methods must be overridden together to maintain the equals-hashCode contract.
```
## List<? extends Number> 
List contains some type that extends Number.

```java
List<Integer>
List<Double>
List<Float>
```
list.add(10); What if double ho. Typesafety toot gyi but we can read it kyuki hai to number hi

## List<? super Integer>
Some parent of Integer.n Could be
```java
List<Integer>
List<Number>
List<Object>
```
Can I write list.add(10); yes because Integer har case me number object yaa Integer hi hoga.
But read nahi kar sakta kya pata double ho


```text
Spring Boot startup ke time component scanning perform karta hai.
 It scans packages under the main application package and looks for annotations such as
@Component, @Service, @Repository, and @RestController. When it finds them, it
creates bean definitions and registers them in the ApplicationContext. Later these beans can be injected using 
Dependency Injection.
```
```text
Creating threads is expensive because thread creation and scheduling involve OS resources.
A thread pool reuses a fixed set of worker threads instead of creating new threads for every task.
Benefits:
- Lower creation overhead
- Better resource utilization
- Controlled concurrency
- Improved throughput
```

Had a Java-focused technical interview at Rakuten recently. Sharing the questions since I couldn't find much recent content on their Java rounds.

The round covered core Java, OOP, collections, Spring Boot, Hibernate, and concurrency. Here's what was asked:

Core Java:

How does autoboxing work? What happens when you compare two Integer objects with ==?
What is the Integer cache in Java? What values does it cache by default?
Explain the difference between String, StringBuilder, and StringBuffer. When would you pick each?
What happens when you call .equals() vs == on String objects?
OOP:
Difference between abstract class and interface in Java 8+. If interfaces can now have default methods, when would you still prefer an abstract class?
Explain method overloading vs method overriding with examples.
Collections:
Internal working of HashMap — how does it handle collisions?
HashMap vs LinkedHashMap vs TreeMap — when to use which?
What's the difference between ArrayList and LinkedList in terms of time complexity for add, get, remove?
Spring Boot & Hibernate:
What is the N+1 problem in Hibernate? How do you fix it?
Explain @Transactional propagation — difference between REQUIRED and REQUIRES_NEW.
Concurrency:
volatile vs synchronized — when is volatile enough?
How would you write a thread-safe singleton? Walk through double-checked locking.
Difference between ReentrantLock and synchronized — when would you prefer ReentrantLock?
Tricky snippet they showed me:
Integer a = 127;
Integer b = 127;
System.out.println(a == b); // ?

Integer x = 128;
Integer y = 128;
System.out.println(x == y); // ?
Be ready to explain why these print different results.