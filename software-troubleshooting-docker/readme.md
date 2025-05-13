docker build -t software-troubleshooting .

docker run -it --name troubleshoot-software software-troubleshooting /troubleshoot/stress_scripts/start_services.sh software

docker exec -it troubleshoot-software bash


# 进程状态排查
ps aux                 # 查看所有进程详细信息
ps aux | grep defunct  # 查找僵死进程
top                    # 动态监控进程资源使用
top -c                 # 显示完整的命令行

# 网络连接和端口占用排查
netstat -tulpn         # 查看所有监听端口和连接
netstat -anp | grep 8080  # 查看8080端口相关连接
ss -tulpn              # 更高效的网络连接查看工具
ss -anp | grep 8080    # 查看8080端口相关连接

# 文件和资源占用排查
lsof -p <PID>          # 查看特定进程打开的文件
lsof -i :8080          # 查看使用8080端口的进程
fuser 8080/tcp         # 查看占用8080端口的进程
fuser /tmp/defunct_process  # 查看使用特定文件的进程