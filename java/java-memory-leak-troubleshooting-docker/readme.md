docker build -t java-memory-leak-troubleshooting .

docker run -it --name troubleshoot-java-memory java-memory-leak-troubleshooting

docker exec -it troubleshoot-java-memory bash

# 查看Java进程ID
jps

# 监控GC活动（每1秒更新一次）
jstat -gc <PID> 1000

# 查看堆内存使用情况
jmap -heap <PID>

# 查看占用内存最多的对象类型（前20）
jmap -histo <PID> | head -20

# 生成堆转储文件
jmap -dump:format=b,file=heapdump.hprof <PID>

# 也可以等待OOM时自动生成（容器已配置-XX:+HeapDumpOnOutOfMemoryError）

# 将堆转储文件复制到宿主机进行分析
docker cp troubleshoot-java-memory:/troubleshoot/heapdump.hprof .

# 使用工具如Eclipse MAT或VisualVM分析堆转储文件

# 查看线程堆栈，确认没有线程阻塞
jstack <PID> > thread_dump.txt

# 分析堆转储文件中的"Leak Suspects"报告
# 查找大量存在的对象（如示例中的LargeObject）
# 分析对象的引用链，找出阻止GC的原因

