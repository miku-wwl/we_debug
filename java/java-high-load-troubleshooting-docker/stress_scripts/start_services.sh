#!/bin/bash
# 启动服务脚本 - 根据参数启动不同的系统负载高场景

case "$1" in
    cpu-load)
        echo "启动CPU密集型负载示例..."
        java -cp /troubleshoot/bin CPUIntensiveLoad > /tmp/cpu_load.log 2>&1 &
        echo "CPU密集型负载示例已启动，PID: $!"
        ;;
    memory-leak)
        echo "启动内存泄漏导致系统负载高示例..."
        java -cp /troubleshoot/bin MemoryLeakExample > /tmp/memory_leak.log 2>&1 &
        echo "内存泄漏导致系统负载高示例已启动，PID: $!"
        ;;
    deadlock)
        echo "启动死锁导致系统负载高示例..."
        java -cp /troubleshoot/bin DeadlockLoadExample > /tmp/deadlock.log 2>&1 &
        echo "死锁导致系统负载高示例已启动，PID: $!"
        ;;
    io-load)
        echo "启动IO密集型负载示例..."
        java -cp /troubleshoot/bin IOIntensiveLoad > /tmp/io_load.log 2>&1 &
        echo "IO密集型负载示例已启动，PID: $!"
        ;;
    context-switch)
        echo "启动上下文切换频繁导致系统负载高示例..."
        java -cp /troubleshoot/bin ContextSwitchLoad > /tmp/context_switch.log 2>&1 &
        echo "上下文切换频繁导致系统负载高示例已启动，PID: $!"
        ;;
    all)
        echo "启动所有系统负载高示例..."
        java -cp /troubleshoot/bin CPUIntensiveLoad > /tmp/cpu_load.log 2>&1 &
        echo "CPU密集型负载示例已启动，PID: $!"
        sleep 1
        java -cp /troubleshoot/bin MemoryLeakExample > /tmp/memory_leak.log 2>&1 &
        echo "内存泄漏导致系统负载高示例已启动，PID: $!"
        sleep 1
        java -cp /troubleshoot/bin DeadlockLoadExample > /tmp/deadlock.log 2>&1 &
        echo "死锁导致系统负载高示例已启动，PID: $!"
        sleep 1
        java -cp /troubleshoot/bin IOIntensiveLoad > /tmp/io_load.log 2>&1 &
        echo "IO密集型负载示例已启动，PID: $!"
        sleep 1
        java -cp /troubleshoot/bin ContextSwitchLoad > /tmp/context_switch.log 2>&1 &
        echo "上下文切换频繁导致系统负载高示例已启动，PID: $!"
        ;;
    none)
        echo "未启动任何系统负载高示例。使用以下命令启动特定故障："
        echo "/troubleshoot/stress_scripts/start_services.sh [cpu-load|memory-leak|deadlock|io-load|context-switch|all]"
        ;;
    *)
        echo "未知参数: $1"
        echo "可用参数: cpu-load, memory-leak, deadlock, io-load, context-switch, all, none"
        exit 1
        ;;
esac

# 保持容器运行
tail -f /dev/null    