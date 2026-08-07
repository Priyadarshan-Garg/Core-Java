## 1. Can you explain what an Operating System is? Why do we need it? Also, if there were no operating system, what problems would a programmer face?

An Operating System is system software that acts as an interface between application programs and the computer hardware.
It manages hardware resources and provides common services to applications through system calls.

Its responsibilities include process management, memory management, file system management, device management, CPU
scheduling, and security.

Without an operating system, every application would need to directly control hardware such as the CPU, memory, disk,
and I/O devices. Developers would have to write hardware-specific code, making software difficult to develop, maintain,
and port across different machines.

---

## 2. Can you explain how an application actually requests a service from the operating system? For example, when a Java program wants to read a file, how does that request reach the operating system?

Applications cannot directly access hardware because they run in user mode, where direct access to CPU, memory, and I/O
devices is restricted for security and stability.

Instead, applications request OS services through system calls.

```text
        Java Application
                │
                ▼
              JVM
                │
        (Java Standard Library)
                │
                ▼
           System Call
                │
                ▼
             Operating System
                │
                ▼
              Kernel
                │
                ▼
              Hardware
```

---

## 3. What is the difference between a Program and a Process? Can multiple processes exist for the same program? Give a real-world example.

A program is a passive entity. It is simply a file stored on disk containing a set of instructions, such as an
executable (.exe) or a Java class (.jar). By itself, a program does not execute.

A process, on the other hand, is an active instance of a program that is currently executing. When a program is
launched, the operating system creates a process for it and allocates resources such as memory, CPU time, open files,
and other system resources.

Yes, multiple processes can exist for the same program. For example, if I open Google Chrome twice, the operating system
creates multiple processes from the same Chrome executable. Each process has its own address space and execution
context, so they are isolated from one another. This isolation ensures that if one process crashes, the others can
continue running.

---

## 4.**Follow-up:** You said each process has its own resources. What exactly are those resources? Can you name them?

Each process has its own virtual address space, which includes the code segment, data segment, heap, and stack. It also
has its own CPU context, such as the program counter and CPU registers, its own Process Control Block (PCB) maintained
by the operating system, and resources like open files, I/O information, and security credentials. These resources allow
the operating system to manage and isolate processes

---

## 5. **Follow-up:** Why does every process need its own CPU registers? There is only one physical CPU.

Although there may be only one physical CPU core, many processes share it through context switching. Before the CPU
switches from one process to another, the operating system saves the current process's CPU context—including its CPU
registers and Program Counter—into that process's Process Control Block (PCB). When the process is scheduled again, the
OS restores this saved context, allowing it to continue execution exactly from where it was interrupted.

---

## 6. **Follow-up:** What is a Process Control Block (PCB)? What information does it contain, and why is it needed?

A Process Control Block (PCB) is a kernel data structure maintained by the operating system to manage a process. Every
process has its own PCB.

It stores all the information required to manage and resume the process, including its Process ID (PID), process state (
Ready, Running, Waiting, etc.), Program Counter, CPU registers, CPU scheduling information (such as priority), memory
management information, and I/O or open file information.

During a context switch, the operating system saves the current CPU context into the PCB of the running process and
restores the context of the next process from its PCB. This allows each process to resume execution exactly from where
it was paused.

---

## 7. **Follow-up:** What are the different states of a process, and can you briefly explain each one?

A process typically goes through the following states:

* **New** (Created): The process is being created. The operating system allocates and initializes the PCB and other
  required resources.
* **Ready**: The process has all the resources it needs except the CPU. It is waiting in the ready queue to be
  scheduled.
* **Running**: The process is currently executing on the CPU.
* **Waiting** (Blocked): The process cannot continue because it is waiting for an event, such as I/O completion, user
  input, or a lock. It is not waiting because of a context switch.
* **Terminated** (Exit): The process has finished execution or has been killed. The operating system releases its
  allocated resources

---

## 8. **Follow-up

** : Can you explain the difference between the Ready state and the Waiting (Blocked) state? They both are "not
running,"

so why are they treated differently by the operating system?

A process in the **Ready** state has all the resources it needs except the CPU. It is waiting in the ready queue for the
scheduler to assign it CPU time.
A process in the **Waiting (Blocked)** state cannot execute even if the CPU is available because it is waiting for an
external event, such as an I/O operation to complete, a network response, or a synchronization event like a lock being
released.Once that event occurs, the process moves back to the Ready state and waits for CPU scheduling.

---

## 9. Follow-up :: Suppose a process is performing a disk read. While the disk is reading the data, is the CPU idle? If not, what does

the operating system do with the CPU?

No. The CPU does not wait for the disk read to complete. When a process initiates an I/O operation, it enters the
Waiting (Blocked) state because it cannot continue until the I/O finishes.
The operating system then performs a context switch and schedules another process from the Ready queue to utilize the
CPU.
The CPU remains busy executing other ready processes. Once the I/O operation completes, the blocked process moves back
to the Ready state, where it waits to be scheduled again.

---

## 10. **Follow-up

** : You have mentioned context switching several times. What exactly is a context switch, and why is it considered an

expensive operation?

A context switch is the process of switching the CPU from one process (or thread) to another. Before the switch, the
operating system saves the current process's CPU context—such as the CPU registers and Program Counter—into its Process
Control Block (PCB). It then restores the context of the next scheduled process from its PCB so that execution can
continue from where it was previously paused.

Context switching is considered expensive because the CPU spends time saving and restoring the execution context instead
of executing useful application code. In addition, switching between processes may require changing the virtual address
space, which can invalidate or reduce the effectiveness of CPU caches and the Translation Lookaside Buffer (TLB),
leading to additional performance overhead.

---

## 11. **Follow-up:** Which is more expensive: a process context switch or a thread context switch, and why?

A process context switch is generally more expensive than a thread context switch.

This is because each process has its own virtual address space and operating system resources. When switching between
processes, the OS has to switch the memory context (address space), restore a different PCB, and this can affect CPU
caches and the TLB.

In contrast, threads within the same process share the same address space, code, heap, and open files. During a thread
context switch, the OS mainly switches the CPU registers, program counter, and stack pointer of the thread. Since the
address space remains the same, the overhead is significantly lower.

---

## 12. **Follow-up

** : If threads share the same address space, what resources are shared among threads, and what resources are private to

each thread?

Threads within the same process share the process's resources, such as the address space, code segment, heap,
global/static variables, and open files.

However, each thread has its own stack, which stores its stack frames, local variables, and method call information.
Each thread also has its own Program Counter (PC) and CPU registers because every thread can be executing a different
sequence of instructions.

---

## 13. **Follow-up

** : If threads share the heap, why do we still get race conditions? Can you explain what a race condition is?

A race condition occurs when two or more threads access the same shared data concurrently, and at least one of them
modifies that data without proper synchronization. As a result, the final outcome depends on the order or timing of
thread execution, making the program's behavior unpredictable.
Example :

```java
count =0

Thread A:count++
Thread B:count++
```

You might expect count to become 2, but it can become 1 because count++ is not an atomic operation.

---

## 14. What is CPU Scheduling, and why do we need it?

CPU Scheduling is the mechanism used by the operating system to decide which process from the Ready queue should be
allocated the CPU next.

We need CPU scheduling because multiple processes may be ready to execute at the same time, but a CPU core can execute
only one process at a time. The scheduler selects the next process based on a scheduling algorithm such as FCFS, SJF,
Priority Scheduling, or Round Robin.

The goals of CPU scheduling are to maximize CPU utilization and throughput, minimize waiting time, turnaround time, and
response time, while ensuring fairness and preventing starvation.

---

## 15. What is the difference between a Preemptive scheduling algorithm and a Non-Preemptive scheduling algorithm?

* Preemptive Scheduling allows the operating system to take the CPU away from a running process before it finishes or
  voluntarily gives up the CPU. This usually happens when:
    * The process's time quantum expires (Round Robin).
    * A higher-priority process arrives (Priority Scheduling).
* Non-Preemptive Scheduling means that once a process gets the CPU, it keeps it until it:
    * Finishes execution, or
    * Blocks (e.g., for I/O).

---

## 16. Follow-up : Can you give me one advantage and one disadvantage of Preemptive Scheduling over Non-Preemptive Scheduling?

**Advantage:**
Preemptive scheduling provides better responsiveness, especially for interactive systems. It also improves CPU
utilization because the CPU can immediately switch to another ready process instead of waiting.
**Disadvantage:**
The main disadvantage is the overhead of context switching. Since the OS can interrupt processes frequently, it spends
additional time saving and restoring process state. If preemption happens too often, performance can degrade

---

## 17. Follow-up: Round Robin is a preemptive scheduling algorithm. Why does it require a Time Quantum? What happens if the Time Quantum is too small or too large?

A **Time Quantum** is the maximum time a process is allowed to execute before the operating system preempts it and
schedules
another process. It is used in Round Robin scheduling to ensure fairness among processes.

If the time quantum is too small, the system performs many context switches, increasing overhead and reducing
throughput.

If the time quantum is too large, processes hold the CPU for longer periods, making Round Robin behave like FCFS and
increasing the response time for other processes.

---

## 18. Why is Round Robin considered a fair scheduling algorithm, whereas SJF (Shortest Job First) can lead to starvation?

**Round Robin** is considered fair because every process in the Ready Queue gets the CPU for a fixed time quantum in a
cyclic manner. No process can hold the CPU indefinitely, so every process eventually gets a chance to execute.

In contrast, Shortest Job First **(SJF)** always selects the process with the smallest CPU burst time. If shorter jobs
keep arriving continuously, a long-running process may never be selected, leading to starvation.

---

## 19. Follow-up : You mentioned starvation. How can we prevent starvation?

Starvation can be prevented using Aging. In aging, the priority of a process is gradually increased the longer it waits
in the Ready Queue. Eventually, even a low-priority or long-waiting process gains enough priority to be scheduled,
preventing indefinite waiting.

---

## 20. What is Virtual Memory? Why do modern operating systems use it instead of letting every process access physical RAM directly?
