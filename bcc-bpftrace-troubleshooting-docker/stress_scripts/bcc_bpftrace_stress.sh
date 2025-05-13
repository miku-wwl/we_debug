#!/bin/bash
# BCC和bpftrace故障模拟脚本 - 演示各种可以通过BCC和bpftrace诊断的问题

echo "启动BCC和bpftrace故障模拟..."

# 1. 网络延迟模拟
echo "启动网络延迟模拟..."
tc qdisc add dev eth0 root netem delay 100ms
echo "已在eth0接口添加100ms网络延迟"

# 2. 文件系统I/O性能问题模拟
echo "启动文件系统I/O性能问题模拟..."
cat << EOF > /tmp/io_stress.c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

#define BUFFER_SIZE 1024*1024

int main() {
    char *buffer = malloc(BUFFER_SIZE);
    int fd = open("/tmp/test_file", O_CREAT | O_RDWR, 0666);
    
    while (1) {
        // 随机读写操作
        if (rand() % 2 == 0) {
            lseek(fd, rand() % (100*1024*1024), SEEK_SET);
            write(fd, buffer, BUFFER_SIZE);
        } else {
            lseek(fd, rand() % (100*1024*1024), SEEK_SET);
            read(fd, buffer, BUFFER_SIZE);
        }
        usleep(10000);
    }
    
    close(fd);
    free(buffer);
    return 0;
}
EOF
gcc /tmp/io_stress.c -o /tmp/io_stress
/tmp/io_stress &
echo "文件系统I/O性能问题模拟已启动，PID: $!"

# 3. 系统调用性能问题模拟
echo "启动系统调用性能问题模拟..."
cat << EOF > /tmp/syscall_stress.c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main() {
    while (1) {
        // 频繁的getpid系统调用
        getpid();
        usleep(100);
    }
    return 0;
}
EOF
gcc /tmp/syscall_stress.c -o /tmp/syscall_stress
/tmp/syscall_stress &
echo "系统调用性能问题模拟已启动，PID: $!"

# 4. 内存分配性能问题模拟
echo "启动内存分配性能问题模拟..."
cat << EOF > /tmp/memory_stress.c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main() {
    while (1) {
        void *ptr = malloc(4096);
        free(ptr);
    }
    return 0;
}
EOF
gcc /tmp/memory_stress.c -o /tmp/memory_stress
/tmp/memory_stress &
echo "内存分配性能问题模拟已启动，PID: $!"

echo "故障模拟已启动"
echo "使用BCC和bpftrace进行故障排查练习"
echo "示例命令:"
echo "  # BCC工具"
echo "  tcplife"
echo "  biolatency"
echo "  execsnoop"
echo "  opensnoop"
echo "  # bpftrace工具"
echo "  bpftrace -e 'tracepoint:syscalls:sys_enter_write { printf(\"%d wrote %d bytes\\n\", pid, args->count); }'"
echo "  bpftrace -e 'kprobe:do_sys_open { printf(\"%s opened by PID %d\\n\", str(args->filename), pid); }'"    