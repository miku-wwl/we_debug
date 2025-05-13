docker build -t java-memory-high-troubleshooting .

# 堆内存泄漏场景
docker run -it --name troubleshoot-memory-heap java-memory-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh heap-leak

# 元空间溢出场景
docker run -it --name troubleshoot-memory-metaspace java-memory-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh metaspace-oom

# 直接内存泄漏场景
docker run -it --name troubleshoot-memory-direct java-memory-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh direct-memory

# Finalizer内存泄漏场景
docker run -it --name troubleshoot-memory-finalizer java-memory-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh finalizer-leak

# String.intern()内存泄漏场景
docker run -it --name troubleshoot-memory-intern java-memory-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh string-intern

# 所有场景
docker run -it --name troubleshoot-memory-all java-memory-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh all

docker exec -it troubleshoot-memory-all bash


案例 1：堆内存泄漏排查
bash
# 步骤1：使用top命令确认内存使用率异常
top

# 步骤2：使用jstat监控GC活动
jstat -gc <PID> 1000

# 步骤3：观察Eden区和老年代持续增长，GC后无明显下降

# 步骤4：使用jmap生成堆转储文件
jmap -dump:format=b,file=heapdump.hprof <PID>

# 步骤5：使用Eclipse MAT或VisualVM分析堆转储文件
# 查找占用内存最大的对象类型和GC Root引用链

# 步骤6：定位到HeapMemoryLeakExample类中的cache对象
案例 2：元空间溢出排查
bash
# 步骤1：使用top命令确认内存使用率异常
top

# 步骤2：使用jstat监控GC活动，发现Full GC频繁
jstat -gc <PID> 1000

# 步骤3：使用jinfo查看JVM参数，确认元空间大小限制
jinfo -flag MetaspaceSize <PID>
jinfo -flag MaxMetaspaceSize <PID>

# 步骤4：使用jmap -histo:live查看类加载信息
jmap -histo:live <PID> | head -20

# 步骤5：发现大量代理类或动态生成的类

# 步骤6：使用jcmd生成类加载统计信息
jcmd <PID> VM.class_hierarchy > class_hierarchy.txt
案例 3：直接内存泄漏排查
bash
# 步骤1：使用top命令确认内存使用率异常，但堆内存正常
top

# 步骤2：使用jstat监控GC活动，发现GC正常但内存持续增长

# 步骤3：使用jmap查看堆内存使用情况，发现堆内存未被充分使用
jmap -heap <PID>

# 步骤4：怀疑直接内存泄漏，检查JVM参数
jinfo -flag MaxDirectMemorySize <PID>

# 步骤5：使用jstack查找与直接内存相关的代码
jstack <PID> | grep -i "directbuffer"

# 步骤6：定位到DirectMemoryLeakExample类中的ByteBuffer.allocateDirect()调用
案例 4：Finalizer 内存泄漏排查
bash
# 步骤1：使用top命令确认内存使用率异常
top

# 步骤2：使用jstat监控GC活动，发现Finalizer队列处理缓慢
jstat -gc <PID> 1000

# 步骤3：使用jmap生成堆转储文件
jmap -dump:format=b,file=heapdump.hprof <PID>

# 步骤4：分析堆转储文件，发现大量等待Finalize的对象

# 步骤5：使用jstack查看Finalizer线程状态
jstack <PID> | grep -A 20 "Finalizer"

# 步骤6：定位到FinalizerLeakExample类中带有finalize方法的BigObject






案例 5：String.intern () 内存泄漏排查
bash
# 步骤1：使用top命令确认内存使用率异常
top

# 步骤2：使用jstat监控GC活动，发现PermGen/Metaspace增长迅速
jstat -gc <PID> 1000

# 步骤3：使用jmap生成堆转储文件
jmap -dump:format=b,file=heapdump.hprof <PID>

# 步骤4：分析堆转储文件，发现大量String对象

# 步骤5：使用工具统计interned字符串
java -XX:+PrintStringTableStatistics -jar YourHeapAnalyzer.jar heapdump.hprof

# 步骤6：定位到StringInternLeakExample类中的String.intern()调用