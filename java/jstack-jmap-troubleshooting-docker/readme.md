# 线程死锁
docker run -it --name troubleshoot-java-deadlock jstack-jmap-troubleshooting /troubleshoot/stress_scripts/start_services.sh deadlock

# CPU密集型
docker run -it --name troubleshoot-java-cpu jstack-jmap-troubleshooting /troubleshoot/stress_scripts/start_services.sh cpu

# 内存泄漏
docker run -it --name troubleshoot-java-memory jstack-jmap-troubleshooting /troubleshoot/stress_scripts/start_services.sh memory

# 线程阻塞
docker run -it --name troubleshoot-java-blocked jstack-jmap-troubleshooting /troubleshoot/stress_scripts/start_services.sh blocked

# 所有故障
docker run -it --name troubleshoot-java-all jstack-jmap-troubleshooting /troubleshoot/stress_scripts/start_services.sh all


docker exec -it troubleshoot-java-all bash


# 查看所有Java进程
jps

# 获取线程堆栈信息
jstack <PID>  # 替换<PID>为Java进程ID

# 连续输出线程堆栈，用于分析CPU高的问题
while true; do jstack <PID> > stack_$(date +%s).txt; sleep 1; done

# 分析死锁
jstack <PID> | grep -A 20 "BLOCKED"
jstack <PID> | grep "deadlock" -A 50 -B 50



# 查看堆内存使用情况
jmap -heap <PID>  # 替换<PID>为Java进程ID

# 生成堆转储文件
jmap -dump:format=b,file=heapdump.hprof <PID>

# 查看对象统计信息
jmap -histo <PID> > histo.txt

# 对于内存泄漏，定期生成堆转储，比较差异
for i in {1..5}; do jmap -dump:format=b,file=heapdump_$i.hprof <PID>; sleep 60; done



# 分析CPU高的线程
top -Hp <PID>  # 找出CPU占用高的线程ID
printf "%x\n" <TID>  # 将线程ID转换为16进制
jstack <PID> | grep <hex_tid> -A 30  # 查找对应线程堆栈

# 使用jhat分析堆转储文件
jhat heapdump.hprof