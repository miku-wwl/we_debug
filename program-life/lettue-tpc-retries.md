在 Redis 主从切换场景中，Lettuce 客户端可能保持着指向旧主库的失效 TCP 连接。当继续使用这些连接发送数据时，由于 TCP 重传机制（受net.ipv4.tcp_retries2内核参数影响，默认值为 15 次重传），数据包会被持续重传直至超时，导致请求长时间阻塞。

https://www.cnblogs.com/wingcode/p/14527107.html