
## 1. What is the difference between JDK, JRE, and JVM?

* JVM (Java Virtual Machine): The abstract machine that executes Java bytecode. It is platform-specific (Windows, Linux,
  macOS each have their own JVM implementation) but executes the same bytecode, giving Java its platform independence.
* JRE (Java Runtime Environment): The runtime environment that includes the JVM plus the core Java class libraries
  needed to run Java programs. You need the JRE to run Java applications, but not to develop them.
* JDK (Java Development Kit): The full development kit that includes the JRE plus development tools like the compiler (
  javac), debugger, and documentation generator (javadoc). You need the JDK to write and compile Java programs.
  A useful memory aid: JDK ⊃ JRE ⊃ JVM. Every JDK contains a JRE, and every JRE contains a JVM.

---

## 2. Describe Java in a single sentence

Java is a platform-independent (write once, run anywhere) object-oriented language with automatic memory management (
garbage collection), strong typing, and a rich standard library.

---

## 3. What are the differences between primitive data types and objects in Java?

Primitives and objects represent two fundamentally different ways Java handles data.

In terms of storage, primitives store actual values while objects store references. Primitives take up less memory,
objects more. Primitives have limited built-in operations while you can implement as many methods as you want for
objects.

Also, primitives can’t be null, limiting their flexibility (depends on the context) while objects can. For their
simplicity, primitives are generally faster to access and manipulate.

In Java, there are 8 primitive types (int, boolean, etc.) while you can create unlimited object types.

---

## 4. What is the difference between String, StringBuilder, and StringBuffer?

If your string is not going to change, use a String class as a String object is immutable.
If your string should be modified and will be accessed only by a single thread, StringBuilder is good
enough. In other scenarios (string can be changed, using multiple threads), use StringBuffer because it is synchronized
and thread-safe.

```java
class Demo {
    public static void main(String[] args) {
        // String (immutable)
        String s = "Hello";
        s += " World"; // Creates a new String object
        // StringBuilder (mutable, not thread-safe)
        StringBuilder sb = new StringBuilder("Hello");
        sb.append(" World"); // Modifies the same object
        // StringBuffer (mutable, thread-safe)
        StringBuffer sbf = new StringBuffer("Hello");
        sbf.append(" World"); // Thread-safe modification
    }
}
```

---

## 5. How do you handle exceptions in Java?

Exceptions in Java can be gracefully handled using try-catch blocks. In the try block, we write the code that might
throw an exception, and the catch block specifies what the code must do if the exception occurs.

A finally block can be used for cleanup operations if try-catch blocks deal with external resources like file managers,
database connections, etc.

Here is a code example demonstrating exception handling in Java:

```java
import java.io.FileReader;
import java.io.IOException;

public class ExceptionHandlingExample {
    public static void main(String[] args) {
        FileReader reader = null;
        try {
            reader = new FileReader("nonexistent.txt");
            // Code that might throw an exception
            int character = reader.read();
            System.out.println((char) character);
        } catch (IOException e) {
            // Handling the specific exception
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        } finally {
            // Cleanup code that always executes
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Error closing file: " + e.getMessage());
                }
            }
        }
    }
}
```

---

## 6. What is the purpose of the static keyword in Java?

The static keyword in Java is used to declare members (variables, methods, nested classes) that belong to the class
itself rather than instances of the class. Declaring static members allows sharing data across all instances of a class,
creating utility methods that don't require object instantiation or defining constraints.

For example, in the following BankAccount class, totalAccounts and INTEREST_RATE are static, so they are shared across
all instances of the class.

```java
public class BankAccount {
    private String accountHolder;
    private double balance;
    private static int totalAccounts = 0;
    private static final double INTEREST_RATE = 0.05;


    // The rest of the code here ...
}
```

---

## 7. Explain the concept of inheritance in Java through examples

Inheritance is one of the core pillars of object-oriented programming in Java.
It allows one class to inherit properties and methods from another class, promoting code reuse and establishing a
parent-child relationship between classes.
For example, Car class may inherit from a general Vehicle class. When doing so, Car can behave just like Vehicle in
terms of attributes and methods like:

* Vehicle class has members like year and make while Car has an additional member transmission.
* Vehicle class has a move method while Car overrides move with additional behavior suited to cars.

---

## 8. What is the difference between == and .equals() when comparing strings?

== compares object references (memory addresses), while .equals() compares the content of strings. For string
comparison, always use .equals().

Here’s an example to illustrate the difference:

```java
 class Demo {

    public static void main(String[] args) {
        String str1 = "Hello";
        String str2 = "Hello";
        String str3 = new String("Hello");
        System.out.println(str1 == str2);       // true (same object reference)
        System.out.println(str1 == str3);       // false (different object references)
        System.out.println(str1.equals(str2));  // true (same content)
        System.out.println(str1.equals(str3));  // true (same content)
    }
}
```

## 9. How do you create and use an array in Java? How do arrays in Java differ from arrays in other languages?

Arrays are critical objects that store multiple values of the same type in Java. They are created using square brackets
and can be initialized in several ways. Here are a couple of common patterns:

1. Declaration and allocation:

```java
    int[] numbers = new int[5];  // Creates an array of 5 integers
```

2. Declaration, allocation, and initialization:

```java
    int[] numbers = {1, 2, 3, 4, 5};  // Creates and initializes an array
```

If we compare Java arrays to Python lists, there are many differences. Here are a couple:

* **Fixed size**: Java arrays, once created, cannot change size
* **Type safety**: Java arrays are type-safe; you can’t put an integer into a String array

---

## 10. What is the purpose of class constructors in Java?

Constructors are special and very important methods used to initialize instances of a class. They have the same name as
the class and are called when a new object is created using the new keyword.

---

## 11. Explain the difference between break and continue statements.

break and continue are important loop flow control keywords in Java. break statement is used to stop the entire loop
immediately and ignore the rest of the loop. It is useful to terminate the loop early based on a condition.

---

## 12. What is method overloading in Java?

Method overloading is a powerful technique that allows a class to have multiple methods with the same name but different
parameters. This lets objects of the class handle closely related tasks with different input types.

Here’s a super short example demonstrating method overloading:

```java
class Calculator {
    int add(int a, int b) {
        return a + b;
    }

    double add(double a, double b) {
        return a + b;
    }
}
```

--- 

## 13. How do you read user input from the console in Java?

Code :

 ```java
     Scanner scanner = new Scanner(System.in);
String input = scanner.nextLine();
```

* Scanner scanner = new Scanner(System.in); This line creates a new Scanner object that reads input from the console (
  System.in).
* String input = scanner.nextLine(); This line reads a full line of text entered by the user and stores it in the input
  variable.

---

## 14. What is the difference between ArrayList and array?

One limitation of Java’s built-in array objects is that their size can’t be changed after initialization. ArrayList
solves this problem and offers more methods for manipulation. However, this comes at the cost of not being able to store
primitives directly. ArrayList only stores objects.

---

## 15. How do you iterate through a collection in Java?

A collection in Java is an object that groups multiple elements into a single unit and part of the Java Collections
Framework. It is often used to store, retrieve, manipulate and communicate aggregate data.

To iterate through a collection, you can use a for-each loop, iterator, or a traditional for loop. The code example
below shows the use of a for-each and a regular for loop:

```java
class Demo {
    public static void main(String[] args) {
        // Example using for-each loop:
        List<String> fruits = Arrays.asList("Apple", "Banana", "Orange");
        for (String fruit : fruits) {
            System.out.println(fruit);
        }
        // Example using regular for loop:
        for (int i = 0; i < fruits.size(); i++) {
            System.out.println(fruits.get(i));
        }
    }
}
```

---

## 16. What is the purpose of the final keyword when used with a variable?

The final keyword in Java is a modifier that can be applied to variables, methods and classes. When used with a
variable, the keyword makes it immutable or in other words, constant. For example, PI is declared as a final variable in
the class below:

```java
public class CircleCalculator {
    private final double PI = 3.14159;

    public double calculateArea(double radius) {
        return PI * radius * radius;
    }
}
```

---

## 17. Explain the difference between public, private, and protected access modifiers.

* public: Accessible from any other class.
* private: Only accessible within the same class.
* protected: Accessible within the same package and by subclasses.
* Default (no modifier): Accessible within the same package only.
  Access modifiers are key in implementing encapsulation, one of the fundamental principles of object-oriented
  programming.

---

## 18. What is the purpose of the this keyword in Java?

I’d like to think of this as a kind of placeholder for a future instance of my class. It is a requirement to use if you
want to differentiate between instance variables and parameters with the same name.

--- 

## 19. How do you convert a string to an integer in Java?

```java
String str = "123";
int num = Integer.parseInt(str);
```

---

## 20. What is the difference between && and & operators?

&& is the logical AND operator with short-circuit evaluation. & is the bitwise AND operator, which also works as a
logical AND but evaluates both sides always.
Code :

```java
class Demo {
    public static void main(String[] args) {
        int a = 5;
        int b = 10;
        boolean result;
        result = (a > 10) && (b++ > 5);
        System.out.println("a = " + a + ", b = " + b + ", result = " + result);
        result = (a > 10) & (b++ > 5);
        System.out.println("a = " + a + ", b = " + b + ", result = " + result);
    }
}
```

Output :

```text
a = 5, b = 10, result = false
a = 5, b = 11, result = false
```

---

## 21. How do you define and use an enum in Java?

```text
enum Day {
   MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}
Day today = Day.MONDAY;
```

--- 

## 22. What is the difference between an abstract class and an interface?

An abstract class can have both abstract and concrete methods, while an interface in Java (before Java 8) could only
contain abstract methods. Starting from Java 8, interfaces can also have default and static methods.
Use Case :

* Use an abstract class when you need to share code between related classes.
* Use an interface when you want to define a contract that unrelated classes can implement.

---

## 23. What is Java? How is it different from other programming languages?

Java is an object-oriented programming (OOP) language that intentionally abstracts the programmer away from the
complexities of the hardware executing the code.
Its platform independence ("Write Once, Run Anywhere") and strong memory management (like garbage collection) are two of
the most differentiating features of the language, making it highly portable and robust.

---

## 24. What is the Java Virtual Machine (JVM)? What is its role in running a Java program?

In simple terms, the JVM is what runs your Java programs. By having one JVM for each system (each OS, each hardware
architecture, etc) you achieve the WORA principle (you write your code once, and run it on many different JVMs).

---

## 25. What are wrapper classes in Java? Why are they used?

Wrapper classes provide a way to use primitive data types (like int, char, boolean) as objects. They are used to enable
primitives to be stored in Java Collections (which only store objects) and to support features like serialization.

---

## 26. Explain the concept of object-oriented programming (OOP). What are its four main principles?

**Object-oriented programming** (OOP) is a way of coding (programming paradigm) based on the concept of "objects," which
can contain data (A.K.A properties) and code (A.K.A methods or behavior). Its four main principles are: Encapsulation,
Inheritance, Polymorphism, and Abstraction.

---

## 27. What is a class in Java? How do you define one?

You can think of a class as a blueprint for creating objects (you define the blueprint once and create many objects with
it). You define one using the class keyword, followed by the class name, and then curly braces containing its members (
fields and methods).

---

## 28. What is an object in Java?

Continuing with the “blueprint” analogy, think of an object as a building built following a pre-defined blueprint. You
can build many buildings with the same blueprint the same way you can instantiate many objects with the same class.

---

## 29. Can a class have multiple constructors?

Yes, a class can have multiple constructors, and that is called “constructor overloading”.

---

## 30. What is the purpose of the new keyword in Java?

The new keyword is used to create an instance of a class (an object) and allocate memory for it on the heap.

---

## 31. Explain the public static void main(String[] args) method. Why is each keyword important?

This is the entry point for any Java program (this method gets called automatically when a Java program is executed).
The entire signature is fixed.

1. **public**: An access modifier, making the method accessible from anywhere.
2. **static**: Allows the method to be called without creating an object of the class. It belongs to the class itself.
3. **void**: Indicates that the method does not return any value.
4. **main**: The standard name of the method recognized by the JVM as the program's starting point.
5. **([] args)**: The parameter for command-line arguments, an array of strings.

---

## 32. What are instance variables and local variables? Where are they stored in memory?

* **Instance variables**: Declared inside a class but outside any method, constructor, or block. Each object has its own
  version of them (they’re not shared between objects). Stored in **Heap memory**.
* **Local variables**: Declared inside a method, constructor, or block. They exist only within that scope. Stored in *
  *Stack memory**.

---

## 33. What are static variables? How do they differ from instance ones?

**Static variables** (also known as class variables) are declared with the static keyword. They belong to the class, not
to any specific object instance. All instances of the class share the same memory location for a static variable,
whereas an instance variable has a unique copy for each object.

---

## 34. What is the static keyword in Java used for? Explain static methods and static members.

The static keyword is used for memory management primarily. It allows a member (variable or method) to belong to the
class itself, rather than to an instance of the class.

* _Static methods_: Can be called directly on the class (e.g., **ClassName.staticMethod()**) without creating an object.
  They don’t have access to instance methods.
* _Static members_: (Variables or methods) are initialized once when the class is loaded.

---

## 35. Can you overload the main method? Why or why not?

Yes, you can overload the **main method** like any other method, but the JVM will only recognize and execute the public
static void main(String[] args) signature as the program's entry point. Other overloaded main methods would need to be
called directly from your code.

---

## 36. What is inheritance in Java? Explain with an example.

Inheritance is an OOP principle where a new class (child/subclass) can inherit properties (fields and methods) from an
existing class (parent/superclass). It helps with code reusability.

--- 

## 37. What is an interface in Java? How does it differ from an abstract class?

An interface is a blueprint of a class. It can contain method signatures, **default and private methods, and static
methods**. It specifies a contract for classes that implement it.
Differences from _abstract classes_: Interfaces have evolved since Java 8. Back then, they were restricted to abstract
methods and constants. With Java 8, static and default methods were introduced, allowing for method implementations
within the interface itself. As of Java 9, private methods were added to help with code reuse among default and static
methods in the interface.

---

## 38. What are abstract classes and abstract methods?

* **Abstract classes**: These are classes declared with the **abstract** keyword. They cannot be instantiated directly
  and can contain both abstract and non-abstract methods. They are meant to be subclassed (such as an Animal class when
  you really want to instantiate Dogs or Cats).
* **Abstract methods**: Methods declared with the abstract keyword inside an abstract class. They have no method
  implementation and must be implemented by the subclasses.

---

## 39. Explain the concept of encapsulation.

Encapsulation bundles a class's attributes and the methods that operate on them, restricting direct access to the data
itself. It hides the internal workings of an object, providing a protective shell that can only be interacted with
through a public interface.
You can achieve it in Java using access modifiers.

---

## 40. What is polymorphism in Java?

Polymorphism allows a single _interface_ to be used for different, related classes. This means you can treat an object
as its general type, even though its specific behavior will be determined by its actual, underlying class. It's the
ability of one name or method to take on multiple forms.

---

## 41. What is a package in Java? Why are they used?

A package is a way to organize related classes and interfaces. They are used for:

* _Modularity_: Grouping related types.
* _Naming Collision Prevention_: Allows classes with the same name to exist in different packages.
* _Access Control_: Control access to classes and members. (Hint: import java.* or import java.util.ArrayList;)

---

## 42. How do you handle errors in Java?

Errors in Java are handled primarily through exception handling using **try, catch, finally, throw, and throws**
keywords.

---

## 43. What is the finally block used for in exception handling?

The **finally** block contains code that will always be executed, regardless of whether an exception occurred in the *
*try** block or not. It's typically used for cleanup operations like closing resources.

---

## 44. What is the purpose of the throw and throws keywords?

* _throw_: Used to explicitly throw an exception from a method or block of code.
* _throws_: Used in a method signature to declare that a method might throw one or more specified types of checked
  exceptions. It informs the caller that they need to handle or declare these exceptions.

---

## 45. What is garbage collection in Java? When does it occur?

**Garbage collection** is an automatic process in Java that reclaims memory occupied by objects that are no longer
referenced by any part of the program. It happens automatically by the JVM when it determines that memory is running low
or during periods of low activity.

---

## 46. What is the difference between == and .equals() in Java?

* **==***: Used for reference comparison (for objects), checking if two references point to the same memory location.
  For primitive types, it compares their values.
* **.equals()**: A method used for content comparison (for objects), checking if two objects are logically "equal" based
  on their values. Its behavior can be overridden by classes.

---

## 47. What is the String class in Java? Is it mutable or immutable?

The String class represents _character_ strings. It is _immutable_, meaning once a String object is created, its content
cannot be changed. Any operation that appears to modify a String actually creates a new String object.

---

## 48. What is the purpose of the super keyword?

The super keyword refers to the immediate parent class of the current object. It's used to:

* Access **parent class** methods or constructors.
* Access parent class instance variables when they are hidden by subclass variables.

---

## 49. Explain the concept of null in Java.

**null** is a special literal that can be assigned to reference variables (objects). It indicates that the variable does
not refer to any object. It represents the absence of a value or a non-existent object.

---

## 50. What makes Java platform independent, and what happens internally from javac Hello.java to running java Hello?

Java source code is compiled by javac into platform-independent bytecode stored in .class files. This bytecode is not
machine code. When we run the program, the JVM loads the bytecode through the class loader, verifies it, and executes
it. Modern JVMs use the JIT compiler to convert frequently executed bytecode into native machine code for better
performance. Since every platform has its own JVM implementation, the same bytecode can run on Windows, Linux, or macOS
without recompilation.

---

## 51. What are the main memory areas inside the JVM? Suppose I write:

```java
public class Test {
    static int x = 10;

    public static void main(String[] args) {
        int a = 5;
        Test t = new Test();
    }
}
```

Where are: x, a, t the Test object stored?

The JVM mainly uses stack and heap memory, but it also has a method area. Local variables like a and references like
t are stored in the stack frame of the executing method. The actual object created using new Test() is allocated on
the heap. Since x is a static variable, it belongs to the class and is stored in the method area (Metaspace in modern
JVMs).

---

## 52. Code

```java
class Animal {
    void sound() {
        System.out.println("Animal");
    }
}

class Dog extends Animal {
    void sound() {
        System.out.println("Dog");
    }

    public static void main(String[] args) {
        Animal a = new Dog();
        a.sound();
    }
}
```

* What will be printed?
* Why does Java choose that method?
* Is this compile-time polymorphism or runtime polymorphism?

At compile time, Java checks whether the method exists in the reference type Animal. Since sound() exists, the code
compiles. At runtime, Java performs dynamic method dispatch and calls the overridden method of the actual object type,
which is Dog. Therefore Dog is printed.

---

## 53. Code

```java
class Parent {
    static void show() {
        System.out.println("Parent");
    }
}

class Child extends Parent {
    static void show() {
        System.out.println("Child");
    }
}

public class Main {
    public static void main(String[] args) {
        Parent p = new Child();
        p.show();
    }
}
```

* What will be printed?
* And more importantly:
* Why is the answer different from the previous question?

Because show() is static. Static methods belong to the class rather than the object. Method overriding works only for
instance methods. Static methods are resolved at compile time using the reference type, so Parent.show() is called.

--- 

## 54. Code

```java
class Demo {
    public static void main(String[] args) {
        String s1 = "hello";
        String s2 = "hello";
        String s3 = new String("hello");
        System.out.println(s1 == s2);
        System.out.println(s1 == s3);
        System.out.println(s1.equals(s3));
    }
}
```

Outcomes and explains why?

s1 and s2 point to the same pooled string object. s3 points to a separate heap object created using new, therefore s1 ==
s3 is false. equals() compares the contents of the strings, so it returns true.

---

## 55. Code

```java
class Demo {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < 1000000; i++) {
            list.add(i);
        }
    }
}
```

* Why is ArrayList insertion usually considered O(1) even though occasionally an insertion can take O(n)?
* What happens internally when the internal array becomes full?

Most insertions happen in constant time because they are appended directly to the backing array. Only when the array
becomes full does ArrayList allocate a larger array and copy elements. Since resizing happens rarely, the average cost
per insertion over many operations remains O(1).

---

## 56. How does HashMap work internally?

* What happens when I do:

```text
    map.put("name", "PD");
```

* How does Java find the value later?
* What is a collision?
* What happens if many keys end up in the same bucket?

HashMap uses the key's hash code.The hash is used to determine a bucket.Multiple keys can end up in the same bucket (
collision). Buckets historically contain linked-list-like nodes.

```text
  index = (n - 1) & hash  
```

where n is the table size (typically a power of 2). Two different hash values can still land in the same bucket.
If the bucket is empty, a new node is placed there. If the bucket already contains nodes, HashMap traverses them, checks
whether the key already exists, updates the value if found, otherwise inserts a new node into that bucket's chain.

---

## 57. Code

```java
class Demo {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < 1000000; i++) {
            list.add(0, i);
        }
    }
}
```

what is the complexity and why?

```text
  list.add(0, i);
```

No. Since we're inserting at index 0, all existing elements must be shifted one position to the right. Therefore each
insertion is O(n), making the overall complexity O(n²).

---

## 58. Explain CAS (Compare-And-Swap) in simple terms. How does Java ensure only one thread successfully updates the value?

CAS works by checking whether the value is still the same as when I last read it. If it is, update it atomically. If it
isn't, another thread has modified it, so the operation fails and retries.

---

## 59. Is HashMap thread-safe? What can go wrong? What would you use instead and why?

HashMap is not thread-safe. If multiple threads perform put and get operations simultaneously, race conditions and
inconsistent data can occur. ConcurrentHashMap is preferred because it allows concurrent access with much better
performance than synchronizing the entire map.

---

## 60. Why it's not true multi threading ?

```java
  Map<String, Integer> map =
      Collections.synchronizedMap(new HashMap<>());
```

It locks the entire map which means only one thread can perform which is not true parallelism.

---

## 61. What does it prints and why ?

```java
    String s1 = "he" + "llo";
    String s2 = "hello";
    
    System.out.println(s1 == s2);
```

The compiler itself simplifies it to:

```java
  String s1 = "hello";
```

before the program even runs. Now both becomes same string and points to same reference in the pool. So answer is true.

---

## 62. What does it prints and why ?
```java
  String s1 = "he";
  String s2 = s1 + "llo";
  String s3 = "hello";
  
  System.out.println(s2 == s3);
  System.out.println(s2.equals(s3));
```
Now this s1 is not a constant so compiler cannot fold it. So at runtime it becomes roughly.
```java
  new StringBuilder()
      .append(s1)
      .append("llo")
      .toString();
```
which creates a new String object. Therefore  s2 has it own object in heap.
This is how memory looks now.
```text
      String Pool
      ┌─────────┐
      │ "hello" │
      └─────────┘
           ↑
           s3
      
      Heap
      ┌─────────┐
      │ "hello" │
      └─────────┘
           ↑
           s2
```
So **s2 == s3** is _false_ and **s2.equals(s3)** is _true_.

---

## 63. What does it prints and why ?
```java
    final String s1 = "he";
    String s2 = s1 + "llo";
    String s3 = "hello";
    System.out.println(s2 == s3);
```
Final makes it compile time constant and compiler folds s1 as **he** at compile time. And replaces it so **s2** becomes **"hello"**.
This returns true.

---
## 64. What could go wrong if I did this ?
```java
    String s = "hello";
    s.setCharAt(0, 'H');
```
Suppose 
```java
    String s1 = "hello";
    String s2 = "hello";
```
```text
         String Pool
        ┌─────────┐
  s1 ──►│ "hello" │◄── s2
        └─────────┘
```
Now if we do **s1.setCharAt(0, 'H')** and get **System.out.println(s2);** we will get **Hello**. But nobody touched
If Strings were mutable, pooling becomes much harder and much less useful.
Another famous example
```java
    Map<String, Integer> map = new HashMap<>();
    String key = "admin";
    map.put(key, 1);
    key.setCharAt(0, 'A');
```
Key changes from **admin** to **Admin** now if we
```java
    map.get("admin");
```
might fail. You inserted it, but can no longer find it.

Thread safety is another benefit : can be safely shared across threads.  No synchronization needed. Nobody can modify it.


---

## 65. What's wrong ? what could be the output, explain why ?
```java
public class Test {

    public static void main(String[] args) {

        Integer a = 127;
        Integer b = 127;

        System.out.println(a == b);

        Integer x = 128;
        Integer y = 128;

        System.out.println(x == y);
    }
}
```
Java maintains an Integer Cache. By default from *-128** to **127** are pre-created and reused.
So
```java
    Integer a = 127;
    Integer b = 127;
```
is 
```java
    Integer a = Integer.valueOf(127);
    Integer b = Integer.valueOf(127);
```
therefore  a == b is **true**.  But 128 is outside default cache so java treat it as different objects. 

--- 

## 66. What are the outputs and explain last line.
```java
      Integer a = 100;
      Integer b = 100;
      
      Integer c = 200;
      Integer d = 200;
      
      System.out.println(a == b);
      System.out.println(c == d);
      
      System.out.println(c.equals(d));

      System.out.println(c == 200);
```
a == b returns **true** because of integer cache. 200 is out side the Integer cache so it will be false.
**c.equals(d)** so it is integer value comparison that's why it will returns **true**.
Last line : c is an object and 200 is a primitive.But Java performs auto-unboxing. Then becomes roughly.
```java
    c.intValue() == 200
```
which is basically **200 == 200** hence it's true. That's why Wrapper classes can be dangerous.
```java
    Integer a = 100;
    Integer b = 100;
    
    a == b
```
They compare references.
```java
    Integer c = 200;
    
    c == 200
```
uses value comparison after unboxing. Same operator **==** different comparison.

---
# 67. What happens ? Compile? Runtime exception? Prints true? Prints false? why ?
```java
    Integer x = null;
    
    System.out.println(x == 0);
```
Compiles successfully. But at runtime: **NullPointerException**
Same auto unboxing happens here.  
```java
    x.intValue() == 0
```
so it becomes 
```java
    null.intValue();
```
JVM explodes.

---
## 68. What is the output and why ?
```java
    Integer a = 100;
    Long b = 100L;
    
    System.out.println(a.equals(b));
```

You may think like
```java
    Integer.valueOf(100).equals(Long.valueOf(100));
```
Eventually it should be true but equals does not work like this.
Internally, Integer.equals() is roughly:
```java
public boolean equals(Object obj) {
    if (obj instanceof Integer) {
        return value == ((Integer) obj).intValue();
    }
    return false;
}
```
Since is a Long, not an Integer: **obj instanceof Integer** fails immediately and **equals** returns false.
```java
  Integer a = 100;
  
  System.out.println(a == 100L);
```
Java auto unbox it and then promotes it to Long which returns **true**.

---

## 69. What prints? Why does HashMap allow one null key but many null values?

```java
    List<String> list = new ArrayList<>();
    
    System.out.println(list.size());
    
    list.add(null);
    list.add(null);
    
    System.out.println(list.size());
    System.out.println(list.contains(null));
```

```java
    System.out.println(list.size());
```
Returns 0
```java
    list.add(null);
    list.add(null);
    System.out.println(list.size());
```
list add both null and the size becomes **2**. If can add then it will return **true** when asked for _contains_.
```java
    map.put(null, 1);
    map.put(null, 2);
```
second line does not create new key it just updates the existing key.

