#!/bin/bash
# 网络压力测试脚本 - 模拟网络流量

echo "启动网络压力测试..."
# 创建一个简单的HTTP服务器
python3 -m http.server 8080 &
echo "HTTP服务器已启动，PID: $!"

# 持续向本地发送网络请求
while true; do
    curl -s http://localhost:8080 > /dev/null
done &
echo "网络请求已启动，PID: $!"    