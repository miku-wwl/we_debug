# 使用OpenJDK作为基础镜像
FROM openjdk:17-jdk-slim

# 设置环境变量，避免交互提示
ENV DEBIAN_FRONTEND=noninteractive

# 更新软件包列表并安装必要的工具
RUN apt-get update
RUN apt-get install -y procps
RUN apt-get install -y htop
RUN apt-get install -y net-tools
RUN apt-get install -y iproute2
RUN apt-get install -y tcpdump
RUN apt-get install -y nmap
RUN apt-get install -y curl
RUN rm -rf /var/lib/apt/lists/*

# 创建工作目录
WORKDIR /troubleshoot

# 复制模拟网络I/O高使用率的Java源代码
COPY src /troubleshoot/src

# 编译Java程序
RUN mkdir -p /troubleshoot/bin && \
    javac -d /troubleshoot/bin /troubleshoot/src/*.java

# 复制启动脚本
COPY stress_scripts /troubleshoot/stress_scripts

# 设置启动脚本为可执行
RUN chmod -R +x *

# 启动脚本，默认情况下不启动任何故障
CMD ["/troubleshoot/stress_scripts/start_services.sh", "none"]    