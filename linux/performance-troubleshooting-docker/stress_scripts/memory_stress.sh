#!/bin/bash
# 内存压力测试脚本 - 占用大量内存

echo "启动内存压力测试..."
stress --vm 1 --vm-bytes 1G --vm-keep --timeout 3600 &
echo "内存压力测试已启动，PID: $!"    