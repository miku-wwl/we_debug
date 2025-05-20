``` java
Communications link failure

The last packet successfully received from the server was 120000 milliseconds ago.
The last packet sent successfully to the server was 120000 milliseconds ago.

java.net.SocketException: Connection reset
	at java.net.SocketInputStream.read(SocketInputStream.java:212)
	at java.net.SocketInputStream.read(SocketInputStream.java:141)
	at com.mysql.cj.protocol.ReadAheadInputStream.readFromUnderlyingStreamIfNecessary(ReadAheadInputStream.java:172)
	at com.mysql.cj.protocol.ReadAheadInputStream.read(ReadAheadInputStream.java:140)
	at com.mysql.cj.protocol.a.NativeProtocol.readFully(NativeProtocol.java:731)
	at com.mysql.cj.protocol.a.NativeProtocol.readPacket(NativeProtocol.java:658)
	at com.mysql.cj.jdbc.ClientPreparedStatement.executeQuery(ClientPreparedStatement.java:902)
	...
```


``` properties
# 基本连接配置
spring.datasource.url=jdbc:mysql://localhost:3306/mydb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# HikariCP核心配置
spring.datasource.hikari.minimum-idle=10
spring.datasource.hikari.maximum-pool-size=100
spring.datasource.hikari.auto-commit=true
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=1500000
spring.datasource.hikari.connection-test-query=SELECT 1
spring.datasource.hikari.connection-timeout = 60000
```