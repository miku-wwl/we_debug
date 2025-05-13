#!/bin/bash
# 软件资源压力测试脚本 - 模拟各种软件资源问题

echo "启动软件资源压力测试..."

# 1. 模拟内存泄漏的进程
echo "启动内存泄漏模拟进程..."
python3 -c '
import os
import time
leak = []
while True:
    leak.append("x" * 1024 * 1024)  # 每次增加1MB内存
    time.sleep(0.1)
' &
echo "内存泄漏进程已启动，PID: $!"

# 2. 模拟僵死进程
echo "创建僵死进程..."
cat << EOF > /tmp/defunct_process.c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main() {
    pid_t pid = fork();
    if (pid < 0) {
        perror("fork failed");
        exit(EXIT_FAILURE);
    } else if (pid == 0) {
        // 子进程立即退出，成为僵死进程
        exit(EXIT_SUCCESS);
    } else {
        // 父进程不回收子进程，让子进程保持僵死状态
        while (1) {
            sleep(1);
        }
    }
    return 0;
}
EOF
gcc /tmp/defunct_process.c -o /tmp/defunct_process
/tmp/defunct_process &
echo "僵死进程已创建，PID: $(ps aux | grep defunct_process | grep -v grep | awk '{print $2}')"

# 3. 模拟端口占用冲突
echo "启动端口占用模拟..."
python3 -m http.server 8080 &
echo "HTTP服务器(端口8080)已启动，PID: $!"
python3 -m http.server 8080 &
echo "尝试在相同端口(8080)启动另一个HTTP服务器，PID: $!"

# 4. 模拟文件描述符耗尽
echo "启动文件描述符耗尽模拟..."
python3 -c '
import os
import time
fds = []
try:
    while True:
        fds.append(os.open("/dev/null", os.O_RDONLY))
except OSError:
    print("无法打开更多文件描述符，已达到系统限制")
    while True:
        time.sleep(1)
' &
echo "文件描述符耗尽模拟已启动，PID: $!"

echo "软件资源压力测试已启动"
echo "使用ps/top命令查看进程状态"
echo "使用netstat/ss命令查看网络连接和端口占用情况"
echo "使用lsof/fuser命令查看文件和资源占用情况"    