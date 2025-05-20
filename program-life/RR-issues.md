``` java
@Transactional(rollbackFor=Exception.class)
public void giveRedPacket(Long orderId){
    Order o = getorder(orderId);
    if (o == null) {
        return;
    }
    o = getorderForUpdate(orderId);
    if (o == null) {
        return;
    }
    if (!existsRedPacket(orderId, redld)) {
        createRedPacket(orderId, redld);
    }
}
```


在 MySQL 默认的 可重复读（RR，Repeatable Read） 事务隔离级别下，基于 MVCC（多版本并发控制）机制，每个事务启动时会创建独立的数据库快照。若两个线程在同一事务中先后执行 getOrder 查询操作，第二个线程（Thread2）会基于自身创建的快照版本读取数据。此时，若第一个线程已创建红包但未提交事务，Thread2 的 existsRedPacket 检查仍会基于旧快照判断红包不存在，导致两个线程均执行 createRedPacket，最终引发红包多发问题。

**解决方案**：
将事务隔离级别改为读已提交（RC，Read Committed）：该级别下，每个语句执行时都会重新获取最新快照，确保 existsRedPacket 能读取到实时数据。
首次查询不纳入事务：将非关键的 getOrder 前置到事务外执行，避免事务内的快照读干扰后续判断。

### 修正方案：基于事务隔离级别调整

根据你补充的红包字段保存在订单表中的情况，问题确实出在RR隔离级别下的MVCC快照读。当两个线程在同一事务中先后读取订单时，后执行的线程会基于MVCC创建自己的快照，导致判断红包是否存在时出现脏读。

#### 方案一：调整事务隔离级别为READ COMMITTED

```java
@Transactional(rollbackFor=Exception.class, isolation = Isolation.READ_COMMITTED)
public void giveRedPacket(Long orderId){
    Order o = getorder(orderId);
    if (o == null) return;
    
    o = getorderForUpdate(orderId);
    if (o == null) return;
    
    if (!existsRedPacket(orderId, redld)) {
        createRedPacket(orderId, redld);
    }
}
```

#### 方案二：第一次查询放在事务外，事务内只处理关键操作

```java
// 非事务方法
public void giveRedPacket(Long orderId){
    // 1. 事务外查询订单基本信息
    Order o = getorder(orderId);
    if (o == null) return;
    
    // 2. 在事务内处理红包逻辑
    doGiveRedPacketInTransaction(orderId);
}

@Transactional(rollbackFor=Exception.class)
protected void doGiveRedPacketInTransaction(Long orderId){
    // 再次查询并加锁
    Order o = getorderForUpdate(orderId);
    if (o == null) return;
    
    if (!existsRedPacket(orderId, redld)) {
        createRedPacket(orderId, redld);
    }
}
```
