## 1. What is a computer actually doing when it "runs a program"?

```textmate
        Problem
           ↓
        Algorithm
           ↓
        Program
           ↓
        Machine instructions
           ↓
        Electrical activity inside CPU
```

---

## 2. So What Is the CPU Actually Doing?

Imagine a CPU with:

* some tiny storage locations called registers
* circuitry capable of performing operations
* circuitry capable of understanding instructions
* a connection to memory

```textmate
        ┌─────────────────┐
        │     Memory      │
        │                 │
        │ instruction 1  │
        │ instruction 2  │
        │ instruction 3  │
        │      ...        │
        └────────┬────────┘
                 │
                 │ instruction
                 ▼
        ┌─────────────────┐
        │       CPU       │
        │                 │
        │  understand it  │
        │       ↓         │
        │  perform it     │
        └─────────────────┘
        Fetch instruction
               ↓
        Understand instruction
               ↓
        Perform instruction
               ↓
        Fetch next instruction
               ↓
        ...
```

---

## 3. Then What Is a "Program"?

**Algorithm** : A method for solving a problem.
**Program** : A concrete implementation of that algorithm, written in a programming language.

```textmate
        Algorithm
           │
           │ implemented as
           ▼
        Program
           │
           │ translated into
           ▼
     Machine instructions
           │
           │ executed by
           ▼
          CPU
```

---

## 4.Where are these instructions stored before the CPU executes them?

```textmate
        SSD
         │
         │ program stored here
         ▼
        RAM
         │
         │ instructions available here
         ▼
        CPU
         │
         │ fetches instructions
         ▼
        Execution
```

Assembly is close to the CPU's instruction set, but the CPU executes the encoded machine instructions, not assembly
text.

---

# Computer As a System

```textmate
                         COMPUTER
                            │
          ┌─────────────────┼─────────────────┐
          │                 │                 │
          ▼                 ▼                 ▼
        CPU              MEMORY              I/O
          │                 │                 │
      executes          stores data       communicates
      instructions      + instructions    with outside world
          │                 │                 │
          └─────────────────┼─────────────────┘
                            │
                     Interconnect / Bus
```

CPU performs :

```textmate
    add
    subtract
    compare
    load
    store
    jump
```

Memory holds:

```textmate
    instructions
    data
    intermediate results 
Like : 
    int x = 10;
    int y = 20;
    int z = x + y;

```

I/O the Computer's Connection outside the world:

```textmate
    Keyboard
    Mouse
    Display
    Disk / SSD
    Network interface
    Printer
    USB devices
    GPU
    etc.
```

How all these communicate : this is just analogy modern Computers don't act like it.

```textmate
                 ┌─────────┐
                 │   CPU   │
                 └────┬────┘
                      │
                      │
                 ┌────▼────┐
                 │  BUS    │
                 └─┬────┬──┘
                   │    │
             ┌─────▼┐  ┌▼─────┐
             │Memory│  │  I/O │
             └──────┘  └──────┘
```

The CPU identifies where information is located, and hardware provides mechanisms to transfer information between
components.

```textmate
    CPU
     │
     │ "I need data at address X"
     ▼
    Interconnect
     │
     ▼
    Memory
     │
     │ finds address X
     │
     │ returns data
     ▼
    Interconnect
     │
     ▼
    CPU
```

## 5. 7. Why Can't We Just Put Everything in One Place? Where

* extremely fast
* extremely large
* extremely cheap
* nonvolatile
  We can't Because physical engineering doesn't give us that for free.
  | Storage | Speed | Capacity | Cost/bit | Persistence |
  | --------- | -------------- | ---------- | ---------- | ----------- |
  | Registers | Extremely fast | Tiny | Very high | No |
  | Cache | Very fast | Small | High | No |
  | RAM | Fast | Larger | Lower | No |
  | SSD/HDD | Much slower | Very large | Much lower | Yes |

**Architecture** = what the machine exposes/behaves like.
**Implementation** = how the hardware actually makes that behaviour happen.
Bigger picture :

```textmate
                 Persistent storage
                        │
                        │ load program
                        ▼
                      Memory
                        │
                        │ fetch instructions/data
                        ▼
                       CPU
                        │
                  execute instructions
                        │
                        ▼
                 result / side effect
                        │
             ┌──────────┴──────────┐
             ▼                     ▼
           Memory                 I/O
```

---

# CPU

It Consists 3 Components: they are not completely independent boxes. They cooperate continuously.

* Registers → very small, very fast storage inside the CPU
* ALU → performs arithmetic and logical operations
* Control Unit → coordinates the CPU's operation

The CPU don't run the program instead the CPU executes instructions belonging to the program.
CPU executing: ADD 5 + 7 it needs to temporarily hold : 5, 7, 12. It could repeatedly access memory so it uses
registers.

```textmate
                    CPU
                     │
             ┌───────▼───────┐
             │   Registers   │
             │               │
             │ R1 = 5        │
             │ R2 = 7        │
             │ R3 = 12       │
             └───────┬───────┘
                     │
                     ▼
                    ALU
```

CPU encounters : R3 = R1 + R2
**ALU** Arithmetic Logic Unit

```textmate
  Arithmetic:
      +
      -
      ×
      etc.
  
  Logic:
      AND
      OR
      XOR
      NOT
  
  Comparisons:
      ==
      <
      >
```

Suppose CPU receives ADD R1, R2, R3 then Control Unit comes into picture:

```textmate
1. Get R1
2. Get R2
3. Tell ALU to perform ADD
4. Take ALU result
5. Put result into R3
```

Image memory contains :

```textmate
Address       Contents

1000          instruction A
1004          instruction B
1008          instruction C
1012          instruction D
```

Program Counter stores : PC = 100 it plays a major role in Context switch

### Modern CPU are more than ALU + CU + Registers

````textmate
CPU
├── Registers
├── Execution units
├── Integer units
├── Floating-point units
├── Load/store units
├── Control logic
├── Caches
├── Branch prediction machinery
├── Pipeline stages
├── Multiple cores
└── Much more
````

CPU and ISA

```textmate
                    CPU
                     │
                     ▼
              understands
                     │
                     ▼
                    ISA
                     │
             ┌───────┴───────┐
             │               │
            x86             ARM
```

### CPU Speed

4 GHz = 4 billion cycles/second but One clock cycle ≠ one instruction.

**Important** : A CPU is hardware that executes instructions belonging to an instruction-set architecture. It contains
registers for fast temporary state, execution hardware such as an ALU for performing operations, and control logic that
coordinates instruction execution. A program counter keeps track of where execution proceeds, and the CPU repeatedly
obtains instructions and executes them.

----

# Registers

Registers are the CPU's immediate workspace.Without registers, the CPU would have to constantly communicate with
external memory for every temporary value.
Register ≠ RAM. THere are two types of registers :

```textmate
General-purpose registers
        │
        ├── numbers
        ├── addresses
        └── temporary values

Special-purpose registers
        │
        ├── Program Counter
        ├── Stack Pointer
        └── Status/flags
```

PC = location from which the CPU will obtain the next instruction.

---

# ALU

```textmate
Conceptually
       R1 = 10 ─────┐
                    │
                    ▼
                 ┌─────┐
                 │ ALU │───► 30 -> R3
                 └─────┘
                    ▲
                    │
       R2 = 20 ─────┘
```

ALU doesn't understand it's own when CPU receives ADD R1, R2, R3. Acutally :

```textmate
                  Instruction
                       │
                       ▼
                Control Logic
                       │
                       │ "perform ADD"
                       ▼
                 ┌──────────┐
                 │   ALU    │
                 └──────────┘
                  ▲        ▲
                  │        │
                 R1       R2
               Control logic tells the ALU what operation to perform; the ALU performs it.
```

ALU + Registers = Actual Computation


---

# Control Unit

Its encoding follows the CPU's instruction-set architecture.

```textmate
┌────────────────────────────────────┐
│ instruction encoding               │
├─────────┬──────────┬───────────────┤
│ opcode  │ operand  │ operand info  │
└─────────┴──────────┴───────────────┘
Because :
opcode = ADD
source = R1
source = R2
destination = R3
```

What is Control Unit :
It interprets the current instruction and generates the control signals necessary to make the appropriate CPU hardware
perform that instruction.
The CPU receives an instruction → control logic determines the required actions → datapath performs them.

--- 

# Machine Instructions

It is CPU-Specific

```textmate
Machine language -> Machine readable
       ▲
       │ assembler
       │
Assembly language -> Human readable
```

What is inside the instruction ?

```textmate
┌──────────┬───────────┬───────────┬─────────────┐
│  opcode  │ source 1  │ source 2  │ destination │ -> not universal format depends on architectures
└──────────┴───────────┴───────────┴─────────────┘
```

OP Code is Operation Code like :

```textmate
opcode
  │
  ├── ADD
  ├── SUB
  ├── LOAD
  ├── STORE
  ├── AND
  ├── OR
  ├── JUMP
  └── ...
```

So this happens internally :

```textmate
ADD R3, R1, R2 Becomes ->
Operation → ADD
Input 1   → R1
Input 2   → R2
Output    → R3
```

Machine instructions can be any

```textmate
calculate
move data
access memory
compare values
change control flow
```

Flow :

```textmate
             SOFTWARE
                 │
                 │ machine instructions
                 ▼
        ┌─────────────────┐
        │       ISA       │
        └─────────────────┘
                 │
                 ▼
              HARDWARE
```

Software can be compiled for an ISA without needing to know every transistor-level detail of the CPU implementing it.

---

# CPU Clock
CPU clock is a timing signal that provides a regular sequence of transitions used to coordinate synchronous digital
logic inside the processor.
```textmate
Clock

   ┌───┐   ┌───┐   ┌───┐   ┌───┐
   │   │   │   │   │   │   │   │
───┘   └───┘   └───┘   └───┘   └───
     ↑       ↑       ↑       ↑
   cycle   cycle   cycle   cycle
```
One period of the clock is called a clock cycle.
1 GHz = 1 Billion cycles per second.
```textmate
2 GHz
   ↓
2 billion cycles/sec
   ↓
0.5 ns per cycle
```
Higher Clock Frequency Does not Mean Faster CPU. It depends on 
```clock frequency
+
instructions per cycle
+
pipeline
+
execution units
+
cache behaviour
+
memory latency
+
branch prediction
+
instruction mix
+
architecture
...
```
Why Can't we increase Ghz because
```textmate
Higher frequency
      ↓
shorter clock period
      ↓
less time for circuits to settle
      ↓
harder timing constraints
```
Modern CPU uses :
```textmate
multiple cores
pipelines
caches
branch prediction
parallel execution
specialised execution units
```
CPU clock is different from OS Timer :
```textmate
CPU hardware
    ↑
  clock
  
  
    Timer
      │
      │ after interval
      ▼
  Interrupt
      │
      ▼
     CPU
```

---
# Bus
A bus is a communication pathway used to transfer information between components of a computer.
**Address Bus** : Where is the information I want?

**Data Bus** :
```textmate
  CPU
   │
   │ address = 5000
   ▼
  Memory
   │
   │ data = 42
   ▼
  CPU
  
Address → identifies the location
Data → carries the actual value
```

**Control Signals** :
```textmate
    CPU
     │
     ├── Address: 5000
     │
     ├── Control: READ
     │
     ▼
    Memory
OR 
    CPU
     │
     ├── Address: 5000
     ├── Data: 42
     └── Control: WRITE
     │
     ▼
    Memory
```
Modern Architecture is more like :
```textmate
                 CPU / Cores
                     │
                CPU interconnect
                     │
              ┌──────┴──────┐
              ▼             ▼
            Cache         Memory
                            │
                         Memory
                        controller
                            │
                    ┌───────┴───────┐
                    ▼               ▼
                  PCIe            other
                    │
             ┌──────┼──────┐
             ▼      ▼      ▼
            GPU    SSD    Network
```

**Bus Bandwidth** : Bandwidth describes how much data can be transferred over a communication path per unit of time.
**Latency** : How long does it take for a particular operation/data transfer to begin producing a result?

---
# RAM
It stores :
* program instructions
* program data
* intermediate values
* operating-system data

