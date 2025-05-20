 虚拟主机&https
``` shell
$ curl -v baidu.com
Host baidu.com:80 was resolved.
IPv6: (none)
IPv4: 39.156.66.10, 110.242.68.66
Trying 39.156.66.10:80 ...
Connected to baidu.com (39.156.66.10) port 80
GET / HTTP/1.1
Host:baidu.ccom
```


``` shell
curl -v 39.156.66.10
Trying 39.156.66.10:80,
Connected to 39.156.66.10 (39.156.66.10) port 80
GET / HTTP/1.1
Host: 39.156.66.10
User-Agent: curl/8.5.0
Accept: */
```


``` shell
$ curl -v 39.156.66.10 -H 'Host: baidu.com'
Trying 39.156.66.10:80 ...
* Connected to 39.156.66.10 (39.156.66.10) port 80
> GET / HTTP/1.1
> Host: baidu.com
> User-Agent: curl/8.5.0
>Accept: */*
<HTTP/1.1 200 0K
< Date: Thu, 27 Mar 2025 07:22:11 GMT 
Server:Apache
```


``` shell
curl -v https://36.152.44.93 -H 'Host: www.baidu.com'

SSL connection using TLSv1.2 / ECDHE-RSA-AES128-GCM-SHA256
prime256v1 / RSASSA-PSS
ALPN: server accepted http/1.1
Server certificate:
subject: C=CN; ST=beijing; L=beijing; O=Beijing Baidu Netc
om Science Technology Co., Ltd; CN=baidu.com 
start date: Jul 8 01:41:02 2024 GMT
expire date: Aug 9 01:41:01 2025 GMT
subjectAltName does not match 36.152.44.93
SSL: no alternative certificate subject name matches target
host name '36.152.44.93'
Closing connection
TLSv1.2 (ouT), TLS alert, close notify (256):
```


``` shell
curl https: //www.baidu.com --resolve www.baidu.com:443:36.152.44.93
```

#### 一、虚拟主机原理
虚拟主机是一种在单一物理服务器上运行多个网站的技术，通过以下机制实现区分：
- **HTTP协议**：使用请求头中的`Host`字段识别目标网站
- **HTTPS协议**：使用TLS握手阶段的`SNI`（Server Name Indication）扩展传递域名信息

#### 二、通信问题解析表
| 问题类型 | 失败场景示例 | 根本原因 | 技术细节 | 解决方案 |
|----------|--------------|----------|----------|----------|
| HTTP直连IP失败 | `curl 39.156.66.10` 返回404或错误页面 | 服务器无法通过IP判断目标虚拟主机 | HTTP/1.1规范要求所有请求必须包含`Host`头，否则服务器默认返回第一个配置的站点或错误 | 1. 使用域名访问（如`curl baidu.com`）<br>2. 手动指定`Host`头（如`curl 39.156.66.10 -H "Host: baidu.com"`） |
| HTTPS直连IP失败 | `curl https://36.152.44.93` 连接超时或证书错误 | 1. TLS握手阶段无法传递域名信息<br>2. 证书校验失败 | 1. 服务器依赖SNI扩展判断使用哪个证书<br>2. 客户端验证证书时要求域名与证书中的CN/SAN字段严格匹配 | 1. 使用域名访问（如`curl https://www.baidu.com`）<br>2. 使用`--resolve`参数强制域名解析（如`curl https://www.baidu.com --resolve www.baidu.com:443:36.152.44.93`） |
| 证书主机名校验失败 | `curl https://36.152.44.93 -H "Host: www.baidu.com"` 返回SSL错误 | 证书中的域名（如`baidu.com`）与访问的IP地址（如`36.152.44.93`）不匹配 | 证书的安全机制要求客户端验证访问的域名与证书中签名的域名一致，防止中间人攻击 | 1. 始终使用域名访问<br>2. 确保证书配置正确（包含所有需要的域名在SAN字段中） |

#### 三、ECH（Encrypted Client Hello）技术说明
ECH是IETF正在标准化的TLS扩展（RFC 8446），主要解决以下问题：
- **隐私泄露风险**：传统HTTPS的Client Hello消息明文包含SNI字段，可能被中间网络窥探用户访问的域名
- **解决方案**：
  1. 客户端使用服务器发布的公钥加密Client Hello消息（包括SNI）
  2. 只有持有对应私钥的目标服务器能解密并获取真实域名
  3. 中间节点只能看到加密的TLS握手流量