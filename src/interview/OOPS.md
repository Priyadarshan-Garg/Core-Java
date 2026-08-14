## 1. What is Object-Oriented Programming, and why do we need OOP? Also explain the difference between a class and an object with a practical Java example.

OOP, or Object-Oriented Programming, is a programming paradigm where we structure software around objects that
encapsulate state and behaviour. It helps us organise large programs by combining related data and the operations that
work on that data.

For example, in Java, we can have a Car class containing properties such as speed and brand, and methods such as
accelerate() and brake(). The class is the blueprint, while an actual Car object created from that class represents a
particular instance with its own state.

---

## 2. Okay. Suppose I have a BankAccount class. What exactly is the difference between the BankAccount class and a BankAccount object? And where does the actual data live

A class is a blueprint or definition that describes the state and behaviour of objects. It can contain fields,
constructors and methods. An object is a runtime instance of that class. Each object can have its own instance state,
while the class defines what behaviour that object supports.

---

## 3. You said an object is a runtime instance of a class. When I write BankAccount account = new BankAccount(); in Java, what exactly happens when new is executed? Where is the object created, where is the reference stored, and what does account actually contain?

* new is a Java keyword used to create an object.
* The object is generally allocated on the heap.
* account is a reference variable referring to that object.
* A local reference like account is typically associated with the current thread's stack frame
* The actual object contains its instance state.

---

## 4. Now suppose BankAccount has a balance field. Why shouldn't I simply make it public and allow anyone to do account.balance = -5000? What problem does encapsulation solve in Java?

Encapsulation means restricting direct access to an object's internal state and providing controlled ways to interact
with it. For example, I would keep the bank account balance private and expose methods such as deposit() and withdraw()
that validate the operation before modifying the balance. This protects the object's invariants and prevents it from
entering an invalid state.

---

## 5. Is encapsulation simply making fields private? If I make every field private and then provide public getters and setters for all of them, have I achieved proper encapsulation? Why or why not?

```java

class BankAccount {
    private double balance;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
```

You've technically restricted direct access, but you haven't properly protected the object's invariant. But it's better

```java
class BankAccount {
    private double balance;

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        }
    }
}
```

----

## 6. So now explain abstraction in Java. What exactly are we hiding, and how is abstraction different from encapsulation? Give me a Java example

Abstraction means exposing only the essential behaviour of an object while hiding its implementation details. In Java,
we commonly achieve abstraction using interfaces and abstract classes. For example, a Payment interface can expose a
pay() method without exposing how the payment is actually processed. Encapsulation, on the other hand, is about
controlling access to the object's internal state.

---

## 7. Which part demonstrates encapsulation and which part demonstrates abstraction? Or does this example demonstrate both? Explain your reasoning.”

```java
class Car {
    private int speed;

    public void accelerate() {
        speed += 10;
    }
}
```

Speed cannot be directly modified from outside the class. The class controls access to its state
There is some abstraction at the usage level: The caller does: **car.accelerate();**
They don't need to manually do: **car.speed += 10;**

---

## 8. Let's move to inheritance. What is inheritance in Java, why do we use it, and what exactly happens when class Dog extends Animal ?

nheritance is a mechanism in Java where a child class derives accessible properties and behaviour from a parent class.
It promotes reuse and allows us to model an ‘is-a’ relationship. When Dog extends Animal, Dog inherits the accessible
members of Animal and can also add its own behaviour or override inherited methods. Private members of Animal are not
directly accessible in Dog.

---

## 9. new Dog(); Which constructor executes first — Animal or Dog? And why?”

```terminaloutput
        new Dog()
           ↓
        Dog constructor starts
           ↓
        super()
           ↓
        Animal constructor
           ↓
        returns to Dog
           ↓
        Dog constructor body
```

**REASON** : the Dog object contains the inherited Animal part as well, and Java initialises the superclass state before
the subclass's own state.
If Animal does not have a no-argument constructor, Java cannot insert super() automatically, and Dog must explicitly
call an available superclass constructor

----

## 10. If Animal does not have a no-argument constructor, Java cannot insert super() automatically, and Dog must explicitly call an available superclass constructor

Only one object is created — a Dog object.
There is no separate Animal object created.
Roughly  :

```terminaloutput
    Allocate ONE Dog object
            ↓
    Initialise Animal portion
            ↓
    Animal constructor
            ↓
    Initialise Dog portion
            ↓
    Dog constructor
```

---

## 11. If I compile this code, will it compile successfully? If not, exactly why not, and how would you fix it?

```java
class Animal {
    Animal(String name) {
        System.out.println(name);
    }
}

class Dog extends Animal {
    Dog() {
        System.out.println("Dog");
    }
}
```

Java implicitly tries to insert: **Super ()**. But Animal doesn't have a no-argument constructor. It only has: Animal(
String name) So compilation fails.
You can do.

```java
class Dog extends Animal {
    Dog() {
        super("Bruno");
        System.out.println("Dog");
    }
}
```

---

## 12. If a child class does not inherit the parent's constructors, then why does the child constructor have to call the parent constructor? And does Dog actually inherit the Animal(String name) constructor?

No, constructors are not inherited in Java. The child constructor calls the parent constructor because the parent part
of the child object also needs to be initialised. The superclass constructor is responsible for initialising the
superclass state. So inheritance of constructors doesn't happen; instead, constructor chaining happens through super().


---

## 13. What will this code print — Animal sound or Bark? More importantly, explain why, considering that the reference type is Animal but the actual object is Dog

```java
class Animal {
    void sound() {
        System.out.println("Animal sound");
    }
}

class Dog extends Animal {
    @Override
    void sound() {
        System.out.println("Bark");
    }
}

void main() {
    Animal a = new Dog();
    a.sound();
}
```

It prints Bark. Although the reference type is Animal, the actual object created is a Dog. Since sound() is an
overridden instance method, Java uses dynamic method dispatch at runtime and invokes the implementation corresponding to
the actual object.

```terminaloutput
    Reference type          Actual object
    Animal                  Dog
       │                      │
       └── determines what    └── determines which overridden
           members are            instance method executes
           accessible
```

---

## 14. What will it print, and why is this different from the method example?”

```java
class Animal {
    String name = "Animal";
}

class Dog extends Animal {
    String name = "Dog";
}

void main() {
    Animal a = new Dog();
    System.out.println(a.name);
}
```

Prints **Animal** because :

* Overridden methods → runtime polymorphism
* Fields → field hiding, not overriding

--- 

## 15. What is the difference between method overloading and method overriding in Java? Don't just give me definitions. Tell me when the decision is made for each one — compile time or runtime — and give me a small example of both.

Method overloading means defining multiple methods with the same name but different parameter lists. The compiler
determines which overloaded method to call, so it is compile-time polymorphism. Method overriding occurs when a subclass
provides its own implementation of an inherited instance method with the same signature. The implementation is selected
at runtime based on the actual object, so it is runtime polymorphism

---

## 16. Can a static method be overridden in Java? Suppose Animal has a static method sound() and Dog defines another static sound() with the same signature. Is that overriding? What happens when I call it through an Animal reference?

No, static methods cannot be overridden because they belong to the class rather than an object. If a subclass defines a
static method with the same signature, it is called method hiding, not overriding. Static method calls are resolved
based on the reference or class type rather than the runtime object.

----

## 17. What happens if I mark an instance method as final in the parent class? Can the child class override it? Why would Java provide final methods?

A final instance method cannot be overridden by a subclass. The purpose of making a method final is to prevent
subclasses from changing behaviour that the parent class considers fixed or essential to its contract. Final methods can
exist in normal classes as well as abstract classes.

---

## 18. Can a final method be overloaded? For example and why ?

* Overriding: “I'm replacing the parent's implementation.” → final blocks this.
* Overloading: “I'm providing another method with a different parameter list.” → final doesn't care.

---

## 19. What is the difference between an interface and an abstract class in Java? And don't just list syntax differences. Suppose you are designing a payment system — when would you choose an interface and when would you choose an abstract class?

An abstract class is useful when related classes share common state or implementation and have a common base identity.
An interface is primarily used to define a contract or capability that different classes can implement. A class can
extend only one class but can implement multiple interfaces. Also, modern Java interfaces can contain default and static
method implementations, so the difference is not simply that interfaces cannot contain implementation.

---

## 20.You said an interface represents a capability. But Java interfaces can have default methods with implementations. So why do we even need abstract classes anymore? Why not just use interfaces for everything?

We still need abstract classes when related classes share a common identity, state, or substantial implementation. An
interface is better when we want to define a capability or contract that unrelated classes can implement. Interfaces
support default implementations, but they generally shouldn't be used as a replacement for a shared object state or a
common base implementation.

---

## 21. Java doesn't allow a class to extend two classes. Why? Suppose both A and B have a method called show(), and class C tries to extend both. What problem could occur? Explain the diamond problem.

Java doesn't support multiple inheritance through classes mainly to avoid ambiguity such as the diamond problem. If a
class inherited the same method through two parent classes, it could become unclear which implementation should be used.
Java allows multiple interfaces instead, and if multiple interfaces provide conflicting default methods, the
implementing class must resolve the conflict explicitly.

```java
class A {
    void show() {
        System.out.println("A");
    }
}

class B extends A {
}

class C extends A {
}

class D extends B, C {
} // ❌ Java doesn't allow this
```

If D calls: **show();** which inherited implementation/path should it use? That's the diamond problem.

But there's an important catch: interfaces can also create a default-method conflict.

```java
interface B {
    default void show() {
        System.out.println("B");
    }
}

interface C {
    default void show() {
        System.out.println("C");
    }
}

class D implements B, C {
    @Override
    public void show() {
        B.super.show(); // explicitly choose
    }
}
```

----

## 22. What exactly does this refer to here? Why do we need this.name = name instead of simply writing name = name ?

this refers to the current object. In this.name = name, this.name refers to the instance variable, while name refers to
the constructor parameter. We need this because the parameter has the same name as the instance field. Without this,
name = name would simply assign the parameter to itself and the object's field would remain unchanged.

---

## 23. We just discussed this. Now tell me the difference between this() and super() inside a constructor. What does each one call, and can you use both in the same constructor?

this() is used to call another constructor of the same class, while super() is used to call a constructor of the parent
class. Both must be the first statement of a constructor, so they cannot be used directly together in the same
constructor. However, constructor chaining can happen where this() eventually leads to a super() call

```terminaloutput
        Student()
           ↓
        this("Unknown")
           ↓
        Student(String)
           ↓
        super()
           ↓
        Object()
```

## 24.Explain the difference between private, default/package-private, protected, and public in Java. But don't just tell me their access levels. Suppose a child class is in a different package from its parent — which members can the child access directly?

| Modifier                | Same class | Same package | Subclass in different package | Anywhere |
|-------------------------|------------|--------------|-------------------------------|----------|
| `private`               | ✅          | ❌            | ❌                             | ❌        |
| default/package-private | ✅          | ✅            | ❌                             | ❌        |
| `protected`             | ✅          | ✅            | ✅*                            | ❌        |
| `public`                | ✅          | ✅            | ✅                             | ✅        |

Default/package-private means: Accessible only within the same package.
Private members are accessible only within the declaring class. Package-private members are accessible within the same
package. Protected members are accessible within the same package and also by subclasses outside the package, subject to
Java's protected-access rules. Public members are accessible from anywhere, provided the class itself is accessible.
---

## 25. Which of these two accesses compile, and which doesn't? Why?

```java
package p1;

public class Parent {
    protected int x = 10;
}
```

```java
package p2;

public class Child extends Parent {

    void test() {
        Child c = new Child();
        System.out.println(c.x);      // ?

        Parent p = new Parent();
        System.out.println(p.x);      // ?
    }
}
```

Result :

```terminaloutput
    Child c = new Child();
    System.out.println(c.x);      // ✅ compiles
    
    Parent p = new Parent();
    System.out.println(p.x);      // ❌ does NOT compile
```

The access through Child c compiles because Child is a subclass of Parent, and protected members are accessible to
subclasses even across packages. The access through Parent p does not compile because, outside the parent package,
protected access cannot be performed through an arbitrary Parent reference.


---

## 26. Every Java class ultimately inherits from which class ? What important methods does it get from that parent?

It inherits Object class by default, methods inherited from Object. Object provides methods such as:

* equals()
* hashCode()
* toString()
* getClass()
* clone()
* wait()
* notify()
* notifyAll()

----

## 27. What will it print and why ?

```terminaloutput
    String a = new String("hello");
    String b = new String("hello");
    
    System.out.println(a == b);
    System.out.println(a.equals(b));
```

Result :

```terminaloutput
    false
    true
```

Reference and content equal.

---

## 28. If I override equals() in my Student class but don't override hashCode(), what problem can occur? Why does Java require equals() and hashCode() to follow a contract?

If I do

```java
class Student {
    int id;

    Student(int id) {
        this.id = id;
    }

    public static void main(String[] args) {
        Student s1 = new Student(101);
        Student s2 = new Student(101);
    }
}
```

Memory has 2 different objects now.

```terminaloutput
    s1 ──────► Student object
               id = 101
    
    s2 ──────► Student object
               id = 101
```

Therefore **s1 == s2** gives _false_. But according to business logic they must be same object due to id.Then we want: *
*s1.equals(s2)** to be true.

```java

@Override
public boolean equals(Object obj) {
    if (this == obj) return true;

    if (!(obj instanceof Student other)) return false;

    return this.id == other.id;
}
```

Now **equals** will return true but we haven't overridden hashcode so

```terminaloutput
    s1
     ↓
    hash 12345
     ↓
    Bucket A
    
    
    s2
     ↓
    hash 67890
     ↓
    Bucket B
```

Now if we add in **HashSet** it should be only 1 object but it's actually 2. If we do **set.add(s2);** it looks like

```terminaloutput
             s2
             ↓
        hashCode()
             ↓
          bucket
             ↓
   Is there a candidate here?
             ↓
       equals() check
```

Now if we had **overridden** hashcode only and not **equals** :

```terminaloutput
            Same hash
                ↓
            same bucket
                ↓
            equals() says false
                ↓
            Both objects remain
```

If didn't override both:

```terminaloutput
s1.equals(s2)     // false
s1.hashCode()     // potentially different
s2.hashCode()     // potentially different
```

**Java Contract** : override both, If a.equals(b) is true, then a.hashCode() MUST equal b.hashCode(). But reverse is NOT
required.Because collisions are allowed.

```terminaloutput
hashCode = same
       ↓
equals MAY be true OR false
```

* hashCode = tells you which locker to search
* equals = tells you whether this is actually the student you're looking for

Suppose **s1.equals(s2) == true** and

```terminaloutput
    s1.hashCode() = 10 // diff bucket
    s2.hashCode() = 20 // diff bucket
```

HashMap/HashSet may never compare them with each other because they went into different buckets.

| `equals()`                | `hashCode()`       | Result                                           |
|---------------------------|--------------------|--------------------------------------------------|
| Neither overridden        | Neither overridden | Identity-based behaviour                         |
| Only `equals()`           | ❌                  | **Broken hash collection contract**              |
| Only `hashCode()`         | ❌                  | Equal objects still not recognised by `equals()` |
| Both correctly overridden | ✅                  | Correct logical equality in `HashMap`/`HashSet`  |

**Interviewer answer** : Because hash-based collections such as HashMap and HashSet use hashCode to locate the bucket
and
then equals to determine logical equality. The Java contract requires that if two objects are equal according to
equals(), they must have the same hashCode. If we override equals() but not hashCode(), two logically equal objects may
produce different hash codes and end up in different buckets, causing HashMap or HashSet to behave incorrectly.

---

## 29. Why is this allowed? Animal is the reference type but Dog is the actual object. What exactly can I access through a, and why can't I directly call a.bark() ?

```java
class Animal {
    void eat() {
        System.out.println("Animal eating");
    }
}

class Dog extends Animal {
    void bark() {
        System.out.println("Dog barking");
    }

    public static void main(String[] args) {
        Animal a = new Dog();
    }
}
```

It looks like

```terminaloutput
    Reference type              Actual object
         Animal  ────────────────►  Dog
```

a can access because The reference type determines what members the compiler allows you to access.

```terminaloutput
    a.eat();    // ✅
    a.bark();   // ❌ compile-time error
```

---

## 30. How can you call bark() through a? What is downcasting, and what would happen if a actually referred to a Cat instead of a Dog?

I know this Animal reference is actually referring to a Dog. Treat it as a Dog. This is how downcasting works

```terminaloutput
    Dog d = (Dog) a;
    d.bark();   // ✅
```

Going from: **Dog → Animal** is _upcasting_.
Going from: **Animal → Dog** is _downcasting_.

**Interview answer** : Downcasting means explicitly converting a superclass reference to a subclass reference. For
example, if Animal a = new Dog(), I can cast it to Dog and access Dog-specific methods. However, the cast is only valid
if the actual object is really a Dog. If the reference points to a Cat and I cast it to Dog, Java throws a
ClassCastException at runtime.
