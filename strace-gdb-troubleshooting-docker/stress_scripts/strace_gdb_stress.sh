#!/bin/bash
# strace和gdb故障模拟脚本 - 演示各种可以通过strace和gdb诊断的问题

echo "启动strace和gdb故障模拟..."

# 1. 段错误程序 - 用于gdb调试
echo "启动段错误程序..."
/troubleshoot/src/segfault > /tmp/segfault.log 2>&1 &
echo "段错误程序已启动，PID: $!"

# 2. 死循环程序 - 用于strace和gdb分析
echo "启动死循环程序..."
/troubleshoot/src/endless_loop > /tmp/endless_loop.log 2>&1 &
echo "死循环程序已启动，PID: $!"

# 3. 资源泄漏程序 - 用于strace和gdb分析
echo "启动资源泄漏程序..."
/troubleshoot/src/resource_leak > /tmp/resource_leak.log 2>&1 &
echo "资源泄漏程序已启动，PID: $!"

# 4. 阻塞系统调用程序 - 用于strace分析
echo "启动阻塞系统调用程序..."
/troubleshoot/src/blocked_syscall > /tmp/blocked_syscall.log 2>&1 &
echo "阻塞系统调用程序已启动，PID: $!"

echo "故障模拟已启动"
echo "使用strace和gdb进行故障排查练习"
echo "示例命令:"
echo "  # strace命令"
echo "  strace -p <PID>"
echo "  strace -f -T -o trace.log /troubleshoot/src/blocked_syscall"
echo "  # gdb命令"
echo "  gdb /troubleshoot/src/segfault <core_file>"
echo "  gdb -p <PID>"
echo "  (gdb) bt"
echo "  (gdb) frame 1"
echo "  (gdb) p ptr"    