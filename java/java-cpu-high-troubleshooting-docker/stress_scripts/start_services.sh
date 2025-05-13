#!/bin/bash
# 启动服务脚本 - 根据参数启动不同的CPU高使用率场景

case "$1" in
    infinite-loop)
        echo "启动死循环示例..."
        java -cp /troubleshoot/bin InfiniteLoopExample > /tmp/infinite_loop.log 2>&1 &
        echo "死循环示例已启动，PID: $!"
        ;;
    regex-backtracking)
        echo "启动正则表达式回溯示例..."
        java -cp /troubleshoot/bin RegexBacktrackingExample > /tmp/regex_backtracking.log 2>&1 &
        echo "正则表达式回溯示例已启动，PID: $!"
        ;;
    frequent-gc)
        echo "启动频繁GC示例..."
        java -Xmx64m -Xms64m -XX:+PrintGCDetails -cp /troubleshoot/bin FrequentGCExample > /tmp/frequent_gc.log 2>&1 &
        echo "频繁GC示例已启动，PID: $!"
        ;;
    deadlock)
        echo "启动死锁导致CPU高使用率示例..."
        java -cp /troubleshoot/bin DeadlockWithHighCPUExample > /tmp/deadlock.log 2>&1 &
        echo "死锁导致CPU高使用率示例已启动，PID: $!"
        ;;
    all)
        echo "启动所有CPU高使用率示例..."
        java -cp /troubleshoot/bin InfiniteLoopExample > /tmp/infinite_loop.log 2>&1 &
        echo "死循环示例已启动，PID: $!"
        sleep 1
        java -cp /troubleshoot/bin RegexBacktrackingExample > /tmp/regex_backtracking.log 2>&1 &
        echo "正则表达式回溯示例已启动，PID: $!"
        sleep 1
        java -Xmx64m -Xms64m -XX:+PrintGCDetails -cp /troubleshoot/bin FrequentGCExample > /tmp/frequent_gc.log 2>&1 &
        echo "频繁GC示例已启动，PID: $!"
        sleep 1
        java -cp /troubleshoot/bin DeadlockWithHighCPUExample > /tmp/deadlock.log 2>&1 &
        echo "死锁导致CPU高使用率示例已启动，PID: $!"
        ;;
    none)
        echo "未启动任何CPU高使用率示例。使用以下命令启动特定故障："
        echo "/troubleshoot/stress_scripts/start_services.sh [infinite-loop|regex-backtracking|frequent-gc|deadlock|all]"
        ;;
    *)
        echo "未知参数: $1"
        echo "可用参数: infinite-loop, regex-backtracking, frequent-gc, deadlock, all, none"
        exit 1
        ;;
esac

# 保持容器运行
tail -f /dev/null    