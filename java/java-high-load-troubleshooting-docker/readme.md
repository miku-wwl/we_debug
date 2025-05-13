使用说明：

构建镜像：

bash
docker build -t java-high-load-troubleshooting .

启动容器并触发特定系统负载高场景：

bash
# CPU密集型负载场景
docker run -it --name troubleshoot-high-cpu java-high-load-troubleshooting /troubleshoot/stress_scripts/start_services.sh cpu-load

# 内存泄漏场景
docker run -it --name troubleshoot-memory-leak java-high-load-troubleshooting /troubleshoot/stress_scripts/start_services.sh memory-leak

# 死锁场景
docker run -it --name troubleshoot-deadlock java-high-load-troubleshooting /troubleshoot/stress_scripts/start_services.sh deadlock

# IO密集型负载场景
docker run -it --name troubleshoot-high-io java-high-load-troubleshooting /troubleshoot/stress_scripts/start_services.sh io-load

# 上下文切换频繁场景
docker run -it --name troubleshoot-context-switch java-high-load-troubleshooting /troubleshoot/stress_scripts/start_services.sh context-switch

# 所有场景
docker run -it --name troubleshoot-all java-high-load-troubleshooting /troubleshoot/stress_scripts/start_services.sh all

进入容器进行故障排查：

bash
docker exec -it troubleshoot-all bash

故障排查练习案例：
案例 1：CPU 密集型负载排查
bash
# 步骤1：使用top命令监控CPU使用情况
top

# 步骤2：找出CPU使用率最高的Java进程PID
# 按1显示所有CPU核心

# 步骤3：使用ps命令确认Java进程
ps aux | grep java

# 步骤4：使用top -Hp <PID>查看占用CPU最高的线程
top -Hp <PID>

# 步骤5：将线程ID转换为16进制
printf "%x\n" <线程ID>

# 步骤6：使用jstack分析线程堆栈
jstack <PID> | grep -A 30 <16进制线程ID>

# 步骤7：定位到CPUIntensiveLoad类中的计算逻辑
案例 2：内存泄漏排查
bash
# 步骤1：使用top命令监控内存使用情况
top

# 步骤2：观察Java进程内存使用是否持续增长
# 使用Shift+M按内存排序

# 步骤3：使用jstat监控GC活动
jstat -gc <PID> 1000

# 步骤4：当内存接近峰值时，生成堆转储
jmap -dump:format=b,file=heapdump.hprof <PID>

# 步骤5：使用jhat或MAT分析堆转储
jhat heapdump.hprof

# 步骤6：访问http://localhost:7000分析堆转储，发现大量byte[]对象
# 定位到MemoryLeakExample类中的leakContainer
案例 3：死锁排查
bash
# 步骤1：使用top命令观察CPU使用率
top

# 步骤2：使用jstack生成线程转储
jstack <PID> > thread_dump.txt

# 步骤3：分析线程转储文件，查找死锁信息
grep -i "deadlock" thread_dump.txt

# 步骤4：查看死锁线程堆栈信息
grep -A 50 -B 50 "Found one Java-level deadlock" thread_dump.txt

# 步骤5：定位到DeadlockLoadExample类中的锁获取顺序问题
案例 4：IO 密集型负载排查
bash
# 步骤1：使用top命令观察CPU和IO等待情况
top

# 步骤2：使用iostat查看磁盘IO情况
iostat -x 1

# 步骤3：使用iotop查看哪个进程占用IO最高
iotop -oP

# 步骤4：使用strace跟踪IO密集型进程
strace -p <PID> -e write

# 步骤5：定位到IOIntensiveLoad类中的文件写入操作





案例 5：上下文切换频繁排查
bash
# 步骤1：使用top命令观察CPU使用率和运行队列
top

# 步骤2：使用vmstat监控系统状态
vmstat 1

# 步骤3：观察cs（上下文切换）和in（中断）列数值
# 高cs值表示上下文切换频繁

# 步骤4：使用pidstat分析每个进程的上下文切换情况
pidstat -w 1

# 步骤5：定位到ContextSwitchLoad类中创建的大量线程