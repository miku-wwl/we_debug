docker build -t bcc-bpftrace-troubleshooting .

docker run -it --name troubleshoot-bcc-bpftrace \
  --privileged \
  -v /lib/modules:/lib/modules \
  -v /sys/kernel/debug:/sys/kernel/debug \
  -v /sys/fs/bpf:/sys/fs/bpf \
  bcc-bpftrace-troubleshooting /troubleshoot/stress_scripts/start_services.sh bcc-bpftrace

docker exec -it troubleshoot-bcc-bpftrace bash

# 监控TCP连接生命周期
tcplife

# 监控磁盘I/O延迟
biolatency

# 监控文件打开操作
opensnoop

# 监控新进程创建
execsnoop

# 监控网络带宽使用
nettop

# 追踪特定函数调用
funccount 'vfs_read'

# 分析系统调用延迟
syscount -t


# 监控write系统调用
bpftrace -e 'tracepoint:syscalls:sys_enter_write { printf("%d wrote %d bytes\n", pid, args->count); }'

# 监控文件打开操作
bpftrace -e 'kprobe:do_sys_open { printf("%s opened by PID %d\n", str(args->filename), pid); }'

# 分析网络延迟
bpftrace -e '
  kprobe:tcp_sendmsg { @start[pid] = nsecs; }
  kretprobe:tcp_sendmsg /@start[pid]/ {
    $duration_us = (nsecs - @start[pid]) / 1000;
    @send_latency = hist($duration_us);
    delete(@start[pid]);
  }
'

# 监控内存分配
bpftrace -e '
  kprobe:kmalloc { @[comm] = count(); }
  kprobe:kfree { @[comm] = count(); }
'

# 分析进程上下文切换
bpftrace -e '
  tracepoint:sched:sched_switch {
    printf(" switch from %s to %s\n", str(args->prev_comm), str(args->next_comm));
  }
'

