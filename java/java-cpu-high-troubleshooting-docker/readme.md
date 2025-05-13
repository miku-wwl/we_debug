docker build -t java-cpu-high-troubleshooting .

# 死循环场景
docker run -it --name troubleshoot-cpu-loop java-cpu-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh infinite-loop

# 正则表达式回溯场景
docker run -it --name troubleshoot-cpu-regex java-cpu-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh regex-backtracking

# 频繁GC场景
docker run -it --name troubleshoot-cpu-gc java-cpu-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh frequent-gc

# 死锁场景
docker run -it --name troubleshoot-cpu-deadlock java-cpu-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh deadlock

# 所有场景
docker run -it --name troubleshoot-cpu-all java-cpu-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh all

docker exec -it troubleshoot-cpu-all bash


案例 1：死循环导致的 CPU 高使用率
# 步骤1：使用top命令找出CPU占用高的进程
top

# 步骤2：记录Java进程ID，使用top -Hp查看线程信息
top -Hp <PID>

# 步骤3：找出CPU占用最高的线程ID，转换为16进制
printf "%x\n" <TID>

# 步骤4：使用jstack获取线程堆栈，查找对应线程
jstack <PID> | grep -A 30 <hex_tid>

# 步骤5：分析堆栈，定位到InfiniteLoopExample类中的死循环


案例 2：正则表达式回溯导致的 CPU 高使用率
# 步骤1：使用top命令确认CPU使用率异常
top

# 步骤2：使用jstat监控GC活动，确认不是GC导致的CPU高
jstat -gc <PID> 1000

# 步骤3：使用jstack获取线程堆栈，查找RUNNABLE状态的线程
jstack <PID> > thread_dump.txt
grep -A 20 "java.lang.Thread.State: RUNNABLE" thread_dump.txt

# 步骤4：分析堆栈，定位到正则表达式相关代码
# 查找java.util.regex包相关的堆栈信息

# 步骤5：使用jcmd生成线程dump并分析
jcmd <PID> Thread.print > thread_dump_jcmd.txt


案例 3：频繁 GC 导致的 CPU 高使用率
# 步骤1：使用top命令确认CPU使用率异常
top

# 步骤2：使用jstat监控GC活动
jstat -gc <PID> 1000

# 步骤3：观察GC频率和GC时间，确认是否GC过于频繁
# 注意观察Eden区、Survivor区和老年代的使用情况

# 步骤4：使用jmap查看堆内存使用情况
jmap -heap <PID>

# 步骤5：生成堆转储文件并分析
jmap -dump:format=b,file=heapdump.hprof <PID>
# 使用工具如Eclipse MAT分析堆转储文件，查找内存泄漏


案例 4：死锁导致的 CPU 高使用率
# 步骤1：使用top命令确认CPU使用率异常
top

# 步骤2：使用jstack获取线程堆栈，查找死锁信息
jstack <PID> | grep -A 50 -B 50 "deadlock"

# 步骤3：分析死锁信息，找出互相等待的线程
# 注意观察线程状态为BLOCKED的线程及其等待的锁

# 步骤4：使用jcmd生成线程dump并分析
jcmd <PID> Thread.print > thread_dump_jcmd.txt

# 步骤5：修复死锁问题（调整锁获取顺序等）