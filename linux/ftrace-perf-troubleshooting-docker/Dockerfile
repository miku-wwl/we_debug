# 使用Ubuntu作为基础镜像
FROM ubuntu:22.04

# 设置环境变量，避免交互提示
ENV DEBIAN_FRONTEND=noninteractive

# 更新软件包列表并安装必要的工具
RUN apt-get update && apt-get install -y linux-tools-generic
RUN apt-get install -y trace-cmd
RUN apt-get install -y build-essential
RUN apt-get install -y git
RUN apt-get install -y stress
RUN apt-get install -y python3
RUN apt-get install -y python3-pip
RUN apt-get install -y curl
RUN rm -rf /var/lib/apt/lists/*

# 创建工作目录
WORKDIR /troubleshoot

# 复制模拟故障的脚本
COPY stress_scripts /troubleshoot/stress_scripts

# 设置启动脚本为可执行
RUN chmod -R +x *

# 启动脚本，默认情况下不启动任何故障
CMD ["/troubleshoot/stress_scripts/start_services.sh", "none"]    