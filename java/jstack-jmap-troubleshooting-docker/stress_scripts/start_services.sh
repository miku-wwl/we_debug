#!/bin/bash
# 启动服务脚本 - 根据参数启动不同的Java故障场景

case "$1" in
    deadlock)
        echo "启动线程死锁示例..."
        java -cp /troubleshoot/bin DeadlockExample > /tmp/deadlock.log 2>&1 &
        echo "线程死锁示例已启动，PID: $!"
        ;;
    cpu)
        echo "启动CPU密集型示例..."
        java -cp /troubleshoot/bin CPUIntensiveExample > /tmp/cpu.log 2>&1 &
        echo "CPU密集型示例已启动，PID: $!"
        ;;
    memory)
        echo "启动内存泄漏示例..."
        java -Xmx256m -cp /troubleshoot/bin MemoryLeakExample > /tmp/memory.log 2>&1 &
        echo "内存泄漏示例已启动，PID: $!"
        ;;
    blocked)
        echo "启动线程阻塞示例..."
        java -cp /troubleshoot/bin BlockedThreadExample > /tmp/blocked.log 2>&1 &
        echo "线程阻塞示例已启动，PID: $!"
        ;;
    all)
        echo "启动所有故障示例..."
        java -cp /troubleshoot/bin DeadlockExample > /tmp/deadlock.log 2>&1 &
        echo "线程死锁示例已启动，PID: $!"
        sleep 1
        java -cp /troubleshoot/bin CPUIntensiveExample > /tmp/cpu.log 2>&1 &
        echo "CPU密集型示例已启动，PID: $!"
        sleep 1
        java -Xmx256m -cp /troubleshoot/bin MemoryLeakExample > /tmp/memory.log 2>&1 &
        echo "内存泄漏示例已启动，PID: $!"
        sleep 1
        java -cp /troubleshoot/bin BlockedThreadExample > /tmp/blocked.log 2>&1 &
        echo "线程阻塞示例已启动，PID: $!"
        ;;
    none)
        echo "未启动任何故障示例。使用以下命令启动特定故障："
        echo "/troubleshoot/stress_scripts/start_services.sh [deadlock|cpu|memory|blocked|all]"
        ;;
    *)
        echo "未知参数: $1"
        echo "可用参数: deadlock, cpu, memory, blocked, all, none"
        exit 1
        ;;
esac

# 保持容器运行
tail -f /dev/null    