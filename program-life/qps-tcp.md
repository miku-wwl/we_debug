压测客户端对web服务进行压测，预期qps > 4000

实际qps远低于预期, 客户端频繁出现 connect timeout、read timeout 问题


统计并显示所有 TCP 连接的状态及其出现次数
``` shell
netstat -nat | awk '/tcp/ {print $6}' | sort | uniq -c
523SYN_RECV
200 ESTABLISHED
6 FIN_WAIT2
11 LAST_ACK
400 TIME_WAIT
```


问题修复
```
# 查看当前TCP半连接队列大小
sysctl net.ipv4.tcp_max_syn_backlog
# 输出应为：net.ipv4.tcp_max_syn_backlog = 128

# 查看当前监听队列大小
sysctl net.core.somaxconn
# 输出应为：net.core.somaxconn = 1024

# 问题：TCP半连接队列远小于监听队列，可能导致SYN洪水攻击下的丢包

# 修复方案：增大半连接队列并优化TCP参数
sudo sysctl -w net.ipv4.tcp_max_syn_backlog=8192
sudo sysctl -w net.core.somaxconn=8192
sudo sysctl -w net.ipv4.tcp_synack_retries=2

# 持久化配置（/etc/sysctl.conf）
echo "net.ipv4.tcp_max_syn_backlog = 8192" | sudo tee -a /etc/sysctl.conf
echo "net.core.somaxconn = 8192" | sudo tee -a /etc/sysctl.conf
echo "net.ipv4.tcp_synack_retries = 2" | sudo tee -a /etc/sysctl.conf
```


在TCP三次握手的标准流程中，Linux内核的实现机制通过两个关键队列对连接状态进行管理：  
1. **SYN半连接队列（SYN Backlog Queue）**：当服务端接收到客户端的初始SYN请求时，会生成一个半连接对象并将其存入该队列，此时连接处于`SYN_RCVD`状态，等待客户端返回ACK确认。  
2. **全连接队列（Accept Queue）**：当服务端收到客户端的ACK报文完成三次握手后，连接状态转为`ESTABLISHED`，此时连接会被移入全连接队列，等待应用层调用`accept()`函数读取处理。  

当**SYN半连接队列已满**时，后续到达的SYN请求会被直接丢弃（或触发重试机制），导致客户端出现`connect timeout`错误——这是因为客户端在规定时间内未收到服务端的SYN+ACK响应。  
而当**全连接队列已满**时，服务端会丢弃客户端的ACK确认报文（此时连接已完成三次握手但未被应用层处理），导致客户端出现`read timeout`错误——因为客户端认为连接已建立，但服务端实际未将其纳入可处理的连接池。  

这种现象常表现为：**服务端监控到的有效连接数远低于预期**，本质是连接在建立阶段因队列容量不足而大量失败。通过调整内核参数（如`net.ipv4.tcp_max_syn_backlog`控制半连接队列长度、`net.core.somaxconn`控制全连接队列长度），扩大队列容量以匹配业务并发需求，可有效解决因队列溢出导致的连接建立失败问题。