docker build -t jstat-jinfo-jcmd-troubleshooting .

# GC压力测试
docker run -it --name troubleshoot-java-gc jstat-jinfo-jcmd-troubleshooting /troubleshoot/stress_scripts/start_services.sh gc-stress

# 堆内存溢出
docker run -it --name troubleshoot-java-heap jstat-jinfo-jcmd-troubleshooting /troubleshoot/stress_scripts/start_services.sh heap-oom

# 元空间溢出
docker run -it --name troubleshoot-java-metaspace jstat-jinfo-jcmd-troubleshooting /troubleshoot/stress_scripts/start_services.sh metaspace-oom

# CPU密集型
docker run -it --name troubleshoot-java-cpu jstat-jinfo-jcmd-troubleshooting /troubleshoot/stress_scripts/start_services.sh cpu

# 所有故障
docker run -it --name troubleshoot-java-all jstat-jinfo-jcmd-troubleshooting /troubleshoot/stress_scripts/start_services.sh all

docker exec -it troubleshoot-java-all bash


# 查看Java进程ID
jps

# 监控GC活动（每1秒更新一次）
jstat -gc <PID> 1000

# 监控类加载统计信息
jstat -class <PID>

# 监控编译统计信息
jstat -compiler <PID>

# 监控GC详细信息
jstat -gccapacity <PID>
jstat -gcutil <PID>


# 查看所有JVM参数
jinfo <PID>

# 查看特定JVM参数
jinfo -flag MaxHeapSize <PID>
jinfo -flag UseG1GC <PID>

# 查看所有系统属性
jinfo -sysprops <PID>


# 查看支持的命令
jcmd <PID> help

# 生成堆转储文件
jcmd <PID> GC.heap_dump heapdump.hprof

# 触发GC
jcmd <PID> GC.run

# 查看线程堆栈
jcmd <PID> Thread.print > thread_dump.txt

# 查看JVM概要信息
jcmd <PID> VM.summary

# 查看JVM命令行标志
jcmd <PID> VM.flags

# 查看GC统计信息
jcmd <PID> GC.class_histogram

