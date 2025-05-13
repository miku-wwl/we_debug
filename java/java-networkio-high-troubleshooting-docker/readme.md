使用说明：

构建镜像：

bash
docker build -t java-networkio-high-troubleshooting .

启动容器并触发特定网络 I/O 高使用率场景：

bash
# HTTP请求高峰场景
docker run -it --name troubleshoot-network-http java-networkio-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh http-spike

# UDP洪泛场景
docker run -it --name troubleshoot-network-udp java-networkio-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh udp-flood

# TCP服务器高负载场景
docker run -it --name troubleshoot-network-tcp java-networkio-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh tcp-server

# 网络序列化性能问题场景
docker run -it --name troubleshoot-network-serialization java-networkio-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh serialization

# DNS查询高峰场景
docker run -it --name troubleshoot-network-dns java-networkio-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh dns-lookup

# 所有场景
docker run -it --name troubleshoot-network-all java-networkio-high-troubleshooting /troubleshoot/stress_scripts/start_services.sh all

进入容器进行故障排查：

bash
docker exec -it troubleshoot-network-all bash

故障排查练习案例：
案例 1：HTTP 请求高峰排查
bash
# 步骤1：使用ss命令查看网络连接状态
ss -tanp | grep java

# 步骤2：使用iftop监控网络流量
iftop -i eth0

# 步骤3：使用tcpdump捕获HTTP流量
tcpdump -i eth0 -s 0 -w http_traffic.pcap port 80 or port 443

# 步骤4：分析捕获的数据包
tcpdump -r http_traffic.pcap | head -20

# 步骤5：使用jstack查看线程堆栈，定位到HttpSpikeExample类
案例 2：UDP 洪泛排查
bash
# 步骤1：使用netstat查看UDP连接
netstat -nup | grep java

# 步骤2：使用nethogs查看进程网络带宽使用
nethogs eth0

# 步骤3：使用tcpdump捕获UDP流量
tcpdump -i eth0 -s 0 -w udp_traffic.pcap udp

# 步骤4：分析UDP数据包内容和目标地址
tcpdump -r udp_traffic.pcap | head -20

# 步骤5：使用jstack查看线程堆栈，定位到UdpFloodExample类
案例 3：TCP 服务器高负载排查
bash
# 步骤1：使用ss命令查看TCP连接状态
ss -tanp | grep :8080

# 步骤2：使用netstat查看连接数统计
netstat -n | awk '/^tcp/ {++S[$NF]} END {for(a in S) print a, S[a]}'

# 步骤3：使用tcpdump捕获TCP流量
tcpdump -i eth0 -s 0 -w tcp_traffic.pcap port 8080

# 步骤4：使用jstack查看线程状态，分析处理线程数量
jstack <PID> | grep -i "runnable" -A 5

# 步骤5：定位到TcpServerExample类，分析连接处理逻辑
案例 4：网络序列化性能问题排查
bash
# 步骤1：使用iftop监控网络流量，发现带宽占用高
iftop -i eth0

# 步骤2：使用tcpdump捕获数据包，发现大量数据传输
tcpdump -i eth0 -s 0 -w serialization.pcap port 9090

# 步骤3：使用jstat监控GC活动，发现频繁GC
jstat -gc <PID> 1000

# 步骤4：使用jmap生成堆转储，分析大对象
jmap -dump:format=b,file=heapdump.hprof <PID>

# 步骤5：定位到NetworkSerializationExample类，分析序列化逻辑
案例 5：DNS 查询高峰排查
bash
# 步骤1：使用ss命令查看UDP连接，发现大量DNS查询
ss -nup | grep 53

# 步骤2：使用tcpdump捕获DNS流量
tcpdump -i eth0 -s 0 -w dns_traffic.pcap port 53

# 步骤3：分析DNS查询内容和频率
tcpdump -r dns_traffic.pcap | grep "A\?"

# 步骤4：使用jstack查看线程堆栈，定位到DnsLookupSpikeExample类

# 步骤5：建议优化DNS缓存策略，减少不必要的查询