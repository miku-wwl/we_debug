# 使用Ubuntu作为基础镜像
FROM ubuntu:22.04

# 设置环境变量，避免交互提示
ENV DEBIAN_FRONTEND=noninteractive

# 更新软件包列表并安装必要的工具
RUN apt-get update && apt-get install -y procps
RUN apt-get install -y sysstat
RUN apt-get install -y dstat
RUN apt-get install -y net-tools
RUN apt-get install -y iotop
RUN apt-get install -y iproute2
RUN apt-get install -y lsof
RUN apt-get install -y ethtool
RUN apt-get install -y python3
RUN apt-get install -y python3-pip
RUN apt-get install -y curl
RUN apt-get install -y tcptrace
RUN apt-get install -y strace
RUN apt-get install -y htop
RUN apt-get install -y stress
RUN rm -rf /var/lib/apt/lists/*


# 创建工作目录
WORKDIR /troubleshoot

# 复制模拟故障的脚本
COPY stress_scripts /troubleshoot/stress_scripts

# 设置启动脚本为可执行
RUN chmod -R +x *

# 启动脚本，默认情况下不启动任何故障
CMD ["/troubleshoot/stress_scripts/start_services.sh", "none"]    