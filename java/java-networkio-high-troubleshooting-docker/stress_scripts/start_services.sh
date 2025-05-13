#!/bin/bash
# 启动服务脚本 - 根据参数启动不同的网络I/O高使用率场景

case "$1" in
    http-spike)
        echo "启动HTTP请求高峰示例..."
        java -cp /troubleshoot/bin HttpSpikeExample > /tmp/http_spike.log 2>&1 &
        echo "HTTP请求高峰示例已启动，PID: $!"
        ;;
    udp-flood)
        echo "启动UDP洪泛示例..."
        java -cp /troubleshoot/bin UdpFloodExample > /tmp/udp_flood.log 2>&1 &
        echo "UDP洪泛示例已启动，PID: $!"
        ;;
    tcp-server)
        echo "启动TCP服务器高负载示例..."
        java -cp /troubleshoot/bin TcpServerExample > /tmp/tcp_server.log 2>&1 &
        echo "TCP服务器高负载示例已启动，PID: $!"
        ;;
    serialization)
        echo "启动网络序列化性能问题示例..."
        java -cp /troubleshoot/bin NetworkSerializationExample > /tmp/serialization.log 2>&1 &
        echo "网络序列化性能问题示例已启动，PID: $!"
        ;;
    dns-lookup)
        echo "启动DNS查询高峰示例..."
        java -cp /troubleshoot/bin DnsLookupSpikeExample > /tmp/dns_lookup.log 2>&1 &
        echo "DNS查询高峰示例已启动，PID: $!"
        ;;
    all)
        echo "启动所有网络I/O高使用率示例..."
        java -cp /troubleshoot/bin HttpSpikeExample > /tmp/http_spike.log 2>&1 &
        echo "HTTP请求高峰示例已启动，PID: $!"
        sleep 1
        java -cp /troubleshoot/bin UdpFloodExample > /tmp/udp_flood.log 2>&1 &
        echo "UDP洪泛示例已启动，PID: $!"
        sleep 1
        java -cp /troubleshoot/bin TcpServerExample > /tmp/tcp_server.log 2>&1 &
        echo "TCP服务器高负载示例已启动，PID: $!"
        sleep 1
        java -cp /troubleshoot/bin NetworkSerializationExample > /tmp/serialization.log 2>&1 &
        echo "网络序列化性能问题示例已启动，PID: $!"
        sleep 1
        java -cp /troubleshoot/bin DnsLookupSpikeExample > /tmp/dns_lookup.log 2>&1 &
        echo "DNS查询高峰示例已启动，PID: $!"
        ;;
    none)
        echo "未启动任何网络I/O高使用率示例。使用以下命令启动特定故障："
        echo "/troubleshoot/stress_scripts/start_services.sh [http-spike|udp-flood|tcp-server|serialization|dns-lookup|all]"
        ;;
    *)
        echo "未知参数: $1"
        echo "可用参数: http-spike, udp-flood, tcp-server, serialization, dns-lookup, all, none"
        exit 1
        ;;
esac

# 保持容器运行
tail -f /dev/null    