#!/bin/bash
# 磁盘压力测试脚本 - 大量写入操作

echo "启动磁盘压力测试..."
dd if=/dev/zero of=/tmp/disk_stress bs=1M count=0 seek=2048 oflag=direct &
echo "磁盘压力测试已启动，PID: $!"    