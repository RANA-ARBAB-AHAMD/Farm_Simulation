Farm Simulation with Concurrent Threads in Java

Developed a multi-threaded farm simulation in Java, where sheep and dogs move concurrently on a grid-based farm. Each animal runs on its own thread, and thread-safe operations are ensured using ReentrantLocks and synchronized collections. The simulation demonstrates real-time animal behavior, escape logic, and interaction between multiple entities.

Key Features:

Concurrency & Thread Safety: Each animal moves independently with safe access to shared cells

Object-Oriented Design: Modular classes for Animal, Sheep, Dog, Cell, and Farm

Dynamic Behavior: Sheep avoid dogs; dogs patrol outer zones

Atomic and synchronized state management: AtomicBoolean flags, synchronizedList for collections

Unit-tested classes for correctness and reliability

Real-time display of farm state with dynamic updates

Technologies & Skills: Java | Threads | ReentrantLock | AtomicBoolean | OOP | Simulation | Synchronization

Outcome:
Simulates realistic animal interactions with thread-safe movement, demonstrating strong concurrent programming, problem-solving, and software design skills.
