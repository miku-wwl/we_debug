构建镜像：

bash
docker build -t java-diskio-high-troubleshooting .

启动容器并触发特定磁盘 I/O 高使用率场景：

bash
# 文件写入高峰场景
docker run -it --name troubleshoot-diskio-write java-diskio-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh file-write

# 随机文件访问场景
docker run -it --name troubleshoot-diskio-random java-diskio-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh random-access

# 日志写入高峰场景
docker run -it --name troubleshoot-diskio-logging java-diskio-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh logging

# 大文件复制场景
docker run -it --name troubleshoot-diskio-copy java-diskio-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh file-copy

# 文件锁争用场景
docker run -it --name troubleshoot-diskio-locking java-diskio-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh file-locking

# 所有场景
docker run -it --name troubleshoot-diskio-all java-diskio-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh all

进入容器进行故障排查：

bash
docker exec -it troubleshoot-diskio-all bash

故障排查练习案例：
案例 1：文件写入高峰排查
bash
# 步骤1：使用iotop确认磁盘I/O高
iotop -oP

# 步骤2：记录Java进程ID，使用iostat查看磁盘性能
iostat -x 1

# 步骤3：使用pidstat查看进程I/O
pidstat -d 1 <PID>

# 步骤4：使用strace跟踪文件操作
strace -p <PID> -e write

# 步骤5：定位到FileWriteSpikeExample类中的文件写入操作
案例 2：随机文件访问排查
bash
# 步骤1：使用iotop确认磁盘I/O高
iotop -oP

# 步骤2：使用iostat查看磁盘await值，确认是否随机I/O
iostat -x 1

# 步骤3：使用lsof查看打开的文件
lsof -p <PID>

# 步骤4：使用jstack查看线程堆栈，定位到RandomFileAccessExample类

# 步骤5：分析随机I/O对磁盘性能的影响
案例 3：日志写入高峰排查
bash
# 步骤1：使用iotop确认磁盘I/O高
iotop -oP

# 步骤2：使用iostat查看磁盘利用率
iostat -x 1

# 步骤3：使用strace跟踪write调用，确认频繁写入
strace -p <PID> -e write

# 步骤4：使用jstack查看线程堆栈，定位到LoggingSpikeExample类

# 步骤5：建议优化日志配置，如增加缓冲区大小、减少flush频率
案例 4：大文件复制排查
bash
# 步骤1：使用iotop确认磁盘I/O高
iotop -oP

# 步骤2：使用iostat查看磁盘吞吐量
iostat -x 1

# 步骤3：使用lsof查看打开的文件，确认大文件操作
lsof -p <PID>

# 步骤4：使用jstack查看线程堆栈，定位到FileCopyExample类

# 步骤5：分析文件复制策略，考虑使用更高效的方法





案例 5：文件锁争用排查
bash
# 步骤1：使用iotop确认磁盘I/O高
iotop -oP

# 步骤2：使用iostat查看磁盘util值，确认是否接近100%
iostat -x 1

# 步骤3：使用jstack查看线程状态，发现大量线程WAITING
jstack <PID> | grep -i "waiting" -A 5

# 步骤4：分析线程堆栈，定位到NioFileLockingExample类

# 步骤5：优化文件锁使用策略，如减少锁持有时间、使用读写锁