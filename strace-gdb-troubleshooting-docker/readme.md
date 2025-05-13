docker build -t strace-gdb-troubleshooting .

docker run -it --name troubleshoot-strace-gdb \
  --cap-add=SYS_PTRACE \
  --security-opt seccomp=unconfined \
  strace-gdb-troubleshooting /troubleshoot/stress_scripts/start_services.sh strace-gdb

docker exec -it troubleshoot-strace-gdb bash

# 跟踪阻塞系统调用的程序
strace -p <PID>  # 替换<PID>为blocked_syscall的进程ID

# 详细跟踪系统调用，包括时间
strace -f -T -o trace.log /troubleshoot/src/blocked_syscall

# 统计系统调用次数
strace -c /troubleshoot/src/resource_leak

# 调试段错误程序
gdb /troubleshoot/src/segfault

# 在gdb中设置核心转储文件
(gdb) core-file /tmp/core  # 如果有核心转储文件

# 附加到正在运行的进程
gdb -p <PID>  # 替换<PID>为endless_loop的进程ID

# 在gdb中执行命令
(gdb) bt  # 查看调用栈
(gdb) frame 1  # 选择调用栈中的帧
(gdb) p ptr  # 打印变量值
(gdb) break func1  # 在func1函数设置断点
(gdb) continue  # 继续执行程序
(gdb) next  # 单步执行
(gdb) step  # 进入函数