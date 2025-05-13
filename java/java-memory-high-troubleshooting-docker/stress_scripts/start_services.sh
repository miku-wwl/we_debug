#!/bin/bash
# 启动服务脚本 - 根据参数启动不同的内存高使用率场景

case "$1" in
    heap-leak)
        echo "启动堆内存泄漏示例..."
        java -Xmx256m -XX:+HeapDumpOnOutOfMemoryError -cp /troubleshoot/bin HeapMemoryLeakExample > /tmp/heap_leak.log 2>&1 &
        echo "堆内存泄漏示例已启动，PID: $!"
        ;;
    metaspace-oom)
        echo "启动元空间溢出示例..."
        java -XX:MetaspaceSize=32m -XX:MaxMetaspaceSize=32m -XX:+HeapDumpOnOutOfMemoryError -cp /troubleshoot/bin MetaspaceOOMExample > /tmp/metaspace_oom.log 2>&1 &
        echo "元空间溢出示例已启动，PID: $!"
        ;;
    direct-memory)
        echo "启动直接内存泄漏示例..."
        java -XX:MaxDirectMemorySize=128m -XX:+HeapDumpOnOutOfMemoryError -cp /troubleshoot/bin DirectMemoryLeakExample > /tmp/direct_memory.log 2>&1 &
        echo "直接内存泄漏示例已启动，PID: $!"
        ;;
    finalizer-leak)
        echo "启动Finalizer内存泄漏示例..."
        java -Xmx256m -XX:+HeapDumpOnOutOfMemoryError -cp /troubleshoot/bin FinalizerLeakExample > /tmp/finalizer_leak.log 2>&1 &
        echo "Finalizer内存泄漏示例已启动，PID: $!"
        ;;
    string-intern)
        echo "启动String.intern()内存泄漏示例..."
        java -Xmx256m -XX:+HeapDumpOnOutOfMemoryError -cp /troubleshoot/bin StringInternLeakExample > /tmp/string_intern.log 2>&1 &
        echo "String.intern()内存泄漏示例已启动，PID: $!"
        ;;
    all)
        echo "启动所有内存高使用率示例..."
        java -Xmx256m -XX:+HeapDumpOnOutOfMemoryError -cp /troubleshoot/bin HeapMemoryLeakExample > /tmp/heap_leak.log 2>&1 &
        echo "堆内存泄漏示例已启动，PID: $!"
        sleep 1
        java -XX:MetaspaceSize=32m -XX:MaxMetaspaceSize=32m -XX:+HeapDumpOnOutOfMemoryError -cp /troubleshoot/bin MetaspaceOOMExample > /tmp/metaspace_oom.log 2>&1 &
        echo "元空间溢出示例已启动，PID: $!"
        sleep 1
        java -XX:MaxDirectMemorySize=128m -XX:+HeapDumpOnOutOfMemoryError -cp /troubleshoot/bin DirectMemoryLeakExample > /tmp/direct_memory.log 2>&1 &
        echo "直接内存泄漏示例已启动，PID: $!"
        sleep 1
        java -Xmx256m -XX:+HeapDumpOnOutOfMemoryError -cp /troubleshoot/bin FinalizerLeakExample > /tmp/finalizer_leak.log 2>&1 &
        echo "Finalizer内存泄漏示例已启动，PID: $!"
        sleep 1
        java -Xmx256m -XX:+HeapDumpOnOutOfMemoryError -cp /troubleshoot/bin StringInternLeakExample > /tmp/string_intern.log 2>&1 &
        echo "String.intern()内存泄漏示例已启动，PID: $!"
        ;;
    none)
        echo "未启动任何内存高使用率示例。使用以下命令启动特定故障："
        echo "/troubleshoot/stress_scripts/start_services.sh [heap-leak|metaspace-oom|direct-memory|finalizer-leak|string-intern|all]"
        ;;
    *)
        echo "未知参数: $1"
        echo "可用参数: heap-leak, metaspace-oom, direct-memory, finalizer-leak, string-intern, all, none"
        exit 1
        ;;
esac

# 保持容器运行
tail -f /dev/null    