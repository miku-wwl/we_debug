#!/bin/bash
# 进程压力测试脚本 - 创建大量进程

echo "启动进程压力测试..."
echo "创建大量进程..."

# 创建多个CPU密集型进程
for i in $(seq 1 10); do
    stress-ng -c 1 -t 3600 &
done

# 创建多个内存密集型进程
for i in $(seq 1 5); do
    stress-ng --vm 1 --vm-bytes 100M --timeout 3600 &
done

echo "进程压力测试已启动"
echo "使用pidstat -u 1监控CPU使用情况"
echo "使用pidstat -r 1监控内存使用情况"
echo "使用pidstat -d 1监控磁盘I/O使用情况"    