docker build -t process-troubleshooting .

docker run -it --name troubleshoot-process process-troubleshooting /troubleshoot/stress_scripts/start_services.sh process

docker exec -it troubleshoot-process bash


# 监控所有进程的CPU使用情况，每秒更新一次
pidstat -u 1

# 监控所有进程的内存使用情况，每秒更新一次
pidstat -r 1

# 监控所有进程的磁盘I/O使用情况，每秒更新一次
pidstat -d 1

# 监控所有进程的上下文切换情况，每秒更新一次
pidstat -w 1

# 只监控stress-ng进程的资源使用情况
pidstat -u 1 -p $(pgrep stress-ng)


``` shell
root@cb3e3c214d88:/troubleshoot# pidstat -u 1
Linux 3.10.0-1160.119.1.el7.x86_64 (cb3e3c214d88)       05/13/25        _x86_64_        (2 CPU)

12:41:47      UID       PID    %usr %system  %guest   %wait    %CPU   CPU  Command
12:41:48        0        67   28.71    0.00    0.00   71.29   28.71     0  stress-ng
12:41:48        0        68   27.72    0.00    0.00   73.27   27.72     0  stress-ng
12:41:48        0        69   35.64    0.00    0.00   62.38   35.64     0  stress-ng
12:41:48        0        70   54.46    0.99    0.00   43.56   55.45     1  stress-ng
12:41:48        0        71   34.65    1.98    0.00   62.38   36.63     0  stress-ng

12:41:48      UID       PID    %usr %system  %guest   %wait    %CPU   CPU  Command
12:41:49        0        67   31.00    0.00    0.00   68.00   31.00     1  stress-ng
12:41:49        0        68   36.00    1.00    0.00   63.00   37.00     1  stress-ng
12:41:49        0        69   35.00    2.00    0.00   64.00   37.00     0  stress-ng
12:41:49        0        70   40.00    1.00    0.00   59.00   41.00     1  stress-ng
12:41:49        0        71   37.00    0.00    0.00   63.00   37.00     0  stress-ng

12:41:49      UID       PID    %usr %system  %guest   %wait    %CPU   CPU  Command
12:41:50        0        67   30.69    0.99    0.00   69.31   31.68     0  stress-ng
12:41:50        0        68   32.67    0.99    0.00   67.33   33.66     1  stress-ng
12:41:50        0        69   42.57    2.97    0.00   53.47   45.54     0  stress-ng
12:41:50        0        70   32.67    0.00    0.00   67.33   32.67     1  stress-ng
12:41:50        0        71   45.54    0.00    0.00   54.46   45.54     0  stress-ng

12:41:50      UID       PID    %usr %system  %guest   %wait    %CPU   CPU  Command
12:41:51        0        67   36.00    0.00    0.00   63.00   36.00     1  stress-ng
12:41:51        0        68   37.00    0.00    0.00   62.00   37.00     1  stress-ng
12:41:51        0        69   44.00    0.00    0.00   57.00   44.00     0  stress-ng
12:41:51        0        70   33.00    0.00    0.00   66.00   33.00     0  stress-ng
12:41:51        0        71   44.00    1.00    0.00   56.00   45.00     0  stress-ng

12:41:51      UID       PID    %usr %system  %guest   %wait    %CPU   CPU  Command
12:41:52        0        67   34.65    0.00    0.00   66.34   34.65     0  stress-ng
12:41:52        0        68   33.66    0.99    0.00   65.35   34.65     1  stress-ng
12:41:52        0        69   50.50    1.98    0.00   47.52   52.48     0  stress-ng
12:41:52        0        70   41.58    0.99    0.00   58.42   42.57     1  stress-ng
12:41:52        0        71   31.68    0.00    0.00   66.34   31.68     1  stress-ng
```