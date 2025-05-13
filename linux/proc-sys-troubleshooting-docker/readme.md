docker build -t proc-sys-troubleshooting .

docker run -it --name troubleshoot-proc-sys \
  --privileged \
  -v /proc:/host/proc \
  -v /sys:/host/sys \
  proc-sys-troubleshooting /troubleshoot/stress_scripts/start_services.sh proc-sys

docker exec -it troubleshoot-proc-sys bash


# 查看系统CPU信息
cat /proc/cpuinfo

# 查看系统内存使用情况
cat /proc/meminfo

# 查看系统负载
cat /proc/loadavg

# 查看所有进程信息
ls /proc | grep '^[0-9]'

# 查看特定进程(PID=123)的详细信息
cat /proc/123/status

# 查看进程打开的文件描述符
ls -l /proc/123/fd

# 查看网络连接信息
cat /proc/net/tcp

# 查看网络接口信息
ls /sys/class/net

# 查看特定网络接口的统计信息
cat /sys/class/net/eth0/statistics/rx_packets
cat /sys/class/net/eth0/statistics/tx_packets

# 查看系统当前TCP参数
cat /sys/module/tcp_advanced_congestion_control/parameters/default

# 查看块设备信息
ls /sys/block

# 查看CPU频率设置
cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor



# 找出占用CPU最高的进程
ps -eo pid,%cpu,comm --sort=-%cpu | head

# 查看进程的内存映射
cat /proc/123/maps

# 查看系统当前打开的文件总数
cat /proc/sys/fs/file-nr

# 监控网络接口流量
watch -n 1 'cat /sys/class/net/eth0/statistics/rx_bytes; cat /sys/class/net/eth0/statistics/tx_bytes'