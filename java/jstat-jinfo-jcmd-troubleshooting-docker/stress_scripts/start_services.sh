#!/bin/bash
# 启动服务脚本 - 根据参数启动不同的Java故障场景

case "$1" in
    gc-stress)
        echo "启动GC压力测试示例..."
        java -Xmx256m -XX:+UseParallelGC -cp /troubleshoot/bin GCStressExample 50 > /tmp/gc_stress.log 2>&1 &
        echo "GC压力测试示例已启动，PID: $!"
        ;;
    heap-oom)
        echo "启动堆内存溢出示例..."
        java -Xmx128m -XX:+HeapDumpOnOutOfMemoryError -cp /troubleshoot/bin HeapOOMExample > /tmp/heap_oom.log 2>&1 &
        echo "堆内存溢出示例已启动，PID: $!"
        ;;
    metaspace-oom)
        echo "启动元空间溢出示例..."
        java -XX:MetaspaceSize=32m -XX:MaxMetaspaceSize=32m -XX:+HeapDumpOnOutOfMemoryError -cp /troubleshoot/bin MetaspaceOOMExample > /tmp/metaspace_oom.log 2>&1 &
        echo "元空间溢出示例已启动，PID: $!"
        ;;
    cpu)
        echo "启动CPU密集型示例..."
        java -cp /troubleshoot/bin CPUIntensiveExample > /tmp/cpu.log 2>&1 &
        echo "CPU密集型示例已启动，PID: $!"
        ;;
    all)
        echo "启动所有故障示例..."
        java -Xmx256m -XX:+UseParallelGC -cp /troubleshoot/bin GCStressExample 50 > /tmp/gc_stress.log 2>&1 &
        echo "GC压力测试示例已启动，PID: $!"
        sleep 1
        java -Xmx128m -XX:+HeapDumpOnOutOfMemoryError -cp /troubleshoot/bin HeapOOMExample > /tmp/heap_oom.log 2>&1 &
        echo "堆内存溢出示例已启动，PID: $!"
        sleep 1
        java -XX:MetaspaceSize=32m -XX:MaxMetaspaceSize=32m -XX:+HeapDumpOnOutOfMemoryError -cp /troubleshoot/bin MetaspaceOOMExample > /tmp/metaspace_oom.log 2>&1 &
        echo "元空间溢出示例已启动，PID: $!"
        sleep 1
        java -cp /troubleshoot/bin CPUIntensiveExample > /tmp/cpu.log 2>&1 &
        echo "CPU密集型示例已启动，PID: $!"
        ;;
    none)
        echo "未启动任何故障示例。使用以下命令启动特定故障："
        echo "/troubleshoot/stress_scripts/start_services.sh [gc-stress|heap-oom|metaspace-oom|cpu|all]"
        ;;
    *)
        echo "未知参数: $1"
        echo "可用参数: gc-stress, heap-oom, metaspace-oom, cpu, all, none"
        exit 1
        ;;
esac

# 保持容器运行
tail -f /dev/null    