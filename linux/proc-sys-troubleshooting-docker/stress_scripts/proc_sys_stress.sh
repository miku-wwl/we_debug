#!/bin/bash
# proc和sys目录故障模拟脚本 - 演示各种可以通过proc和sys目录诊断的问题

echo "启动proc和sys目录故障模拟..."

# 1. CPU负载压力测试
echo "启动CPU压力测试..."
stress --cpu 2 --timeout 3600 &
echo "CPU压力测试已启动，PID: $!"

# 2. 内存泄漏模拟
echo "启动内存泄漏模拟..."
python3 -c '
import os
import time
leak = []
while True:
    leak.append("x" * 1024 * 1024)  # 每次增加1MB内存
    time.sleep(0.1)
' &
echo "内存泄漏模拟已启动，PID: $!"

# 3. 网络连接问题模拟
echo "启动网络连接问题模拟..."
python3 -c '
import socket
import time

# 创建大量TCP连接
sockets = []
for i in range(1000):
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect(("127.0.0.1", 8080))
        sockets.append(s)
    except:
        pass
    time.sleep(0.1)

# 保持所有连接打开
while True:
    time.sleep(1)
' &
echo "网络连接问题模拟已启动，PID: $!"

# 4. 修改sysctl参数制造问题
echo "修改sysctl参数模拟网络问题..."
# 降低TCP连接队列长度
sysctl -w net.core.somaxconn=10 > /dev/null
# 限制文件描述符数量
ulimit -n 1024

# 5. 模拟磁盘I/O问题
echo "启动磁盘I/O问题模拟..."
dd if=/dev/zero of=/tmp/io_stress bs=1M count=0 seek=512 oflag=direct &
echo "磁盘I/O问题模拟已启动，PID: $!"

echo "故障模拟已启动"
echo "使用/proc和/sys目录进行故障排查练习"
echo "示例命令:"
echo "  cat /proc/cpuinfo"
echo "  cat /proc/meminfo"
echo "  cat /proc/loadavg"
echo "  cat /proc/<PID>/status"
echo "  cat /proc/<PID>/fd"
echo "  ls /sys/class/net"
echo "  cat /sys/class/net/eth0/statistics/rx_errors"
echo "  sysctl -a"    