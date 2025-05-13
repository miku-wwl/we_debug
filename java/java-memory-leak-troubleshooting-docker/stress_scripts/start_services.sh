#!/bin/bash
# 启动内存泄漏示例

echo "启动Java内存泄漏示例..."
echo "提示: 使用以下命令进行故障排查"
echo "  jps - 查看Java进程ID"
echo "  jstat -gc <PID> 1000 - 监控GC活动"
echo "  jmap -heap <PID> - 查看堆内存使用情况"
echo "  jmap -histo <PID> | head -20 - 查看对象分布"
echo "  jmap -dump:format=b,file=heapdump.hprof <PID> - 生成堆转储文件"
echo "  jstack <PID> - 查看线程堆栈"

# 启动Java程序，限制堆内存以加速问题出现
java -Xmx256m -Xms256m -XX:+HeapDumpOnOutOfMemoryError -cp /troubleshoot/bin MemoryLeakExample > /tmp/memory_leak.log 2>&1 &

# 记录PID
echo $! > /tmp/memory_leak.pid
echo "Java进程已启动，PID: $(cat /tmp/memory_leak.pid)"

# 保持容器运行
tail -f /dev/null    