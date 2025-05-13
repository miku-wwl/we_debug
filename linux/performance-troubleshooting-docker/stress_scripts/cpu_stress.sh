#!/bin/bash
# CPU压力测试脚本 - 持续100% CPU使用率

echo "启动CPU压力测试..."
stress --cpu 4 --timeout 3600 &
echo "CPU压力测试已启动，PID: $!"    