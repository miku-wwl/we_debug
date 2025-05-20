 | 隔离级别       | 解决的主要问题                | 实现方案                  | 作用与特点                                                                 |
|----------------|-----------------------------|-------------------------|--------------------------------------------------------------------------|
| **未提交读**   | 无（允许脏读、脏写）          | 读不加锁，写加排他锁      | 最低隔离级别，性能最高，但可能读取到未提交数据，存在脏读、脏写风险。               |
| **提交读**     | 脏读                        | MVCC（多版本并发控制）     | 确保读取到已提交的数据，避免脏读，但可能出现不可重复读（前后两次读取结果不一致）。       |
| **可重复读**   | 脏读、不可重复读、部分幻读    | MVCC + 间隙锁（Next-Key Lock） | 事务内多次读取数据一致，避免不可重复读；通过间隙锁解决部分幻读问题（MySQL InnoDB默认）。 |
| **序列化**     | 脏读、不可重复读、幻读、写倾斜 | 两阶段锁（2PL）+ 死锁检测    | 最高隔离级别，通过锁保证串行化执行，彻底避免所有并发问题，但性能最低。               |


| Isolation Level       | Primary Issues Solved                | Implementation Method         | Role and Characteristics                                                                 |
|-----------------------|-------------------------------------|-------------------------------|------------------------------------------------------------------------------------------|
| **Read Uncommitted**  | None (allows dirty reads/writes)     | No read locks, write with exclusive locks | Lowest isolation level, highest performance, but may read uncommitted data, risking dirty reads/writes. |
| **Read Committed**    | Dirty Reads                         | MVCC (Multiversion Concurrency Control)  | Ensures reading committed data, avoiding dirty reads, but may cause non-repeatable reads (inconsistent results across reads). |
| **Repeatable Read**   | Dirty Reads, Non-repeatable Reads, Partial Phantom Reads | MVCC + Gap Locks (Next-Key Lock) | Ensures consistent data within a transaction, avoiding non-repeatable reads; solves partial phantom reads via gap locks (default in MySQL InnoDB). |
| **Serializable**      | Dirty Reads, Non-repeatable Reads, Phantom Reads, Write Skew | Two-Phase Locking (2PL) + Deadlock Detection | Highest isolation level, ensuring serial execution via locks to completely avoid all concurrency issues, but with the lowest performance. |


在应用层也有许多解决方案，比如：
原子操作
乐观锁(CAS)
悲观锁
唯一索引
队列+单线程执行
实体化冲突