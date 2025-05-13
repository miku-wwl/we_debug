#!/bin/bash
# ftrace和perf故障模拟脚本 - 演示各种可以通过ftrace和perf诊断的性能问题

echo "启动ftrace和perf故障模拟..."

# 1. CPU热点函数模拟 - 计算密集型任务
echo "启动CPU热点函数模拟..."
cat << EOF > /tmp/cpu_hotspot.c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

// 热点函数 - 大量的整数计算
void hot_function() {
    int i, j, sum = 0;
    for (i = 0; i < 10000; i++) {
        for (j = 0; j < 10000; j++) {
            sum += i * j;
        }
    }
}

// 普通函数
void normal_function() {
    sleep(1);
}

int main() {
    while (1) {
        hot_function();
        normal_function();
    }
    return 0;
}
EOF
gcc -O0 -g /tmp/cpu_hotspot.c -o /tmp/cpu_hotspot
/tmp/cpu_hotspot &
echo "CPU热点函数模拟已启动，PID: $!"

# 2. 内存分配热点模拟
echo "启动内存分配热点模拟..."
cat << EOF > /tmp/memory_hotspot.c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

// 内存分配热点函数
void memory_allocation_hotspot() {
    int i;
    for (i = 0; i < 10000; i++) {
        void *ptr = malloc(1024);
        free(ptr);
    }
}

int main() {
    while (1) {
        memory_allocation_hotspot();
        sleep(1);
    }
    return 0;
}
EOF
gcc -O0 -g /tmp/memory_hotspot.c -o /tmp/memory_hotspot
/tmp/memory_hotspot &
echo "内存分配热点模拟已启动，PID: $!"

# 3. 系统调用延迟模拟
echo "启动系统调用延迟模拟..."
cat << EOF > /tmp/syscall_delay.c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main() {
    while (1) {
        // 频繁的write系统调用
        write(1, ".", 1);
        usleep(1000);
    }
    return 0;
}
EOF
gcc -O0 -g /tmp/syscall_delay.c -o /tmp/syscall_delay
/tmp/syscall_delay &
echo "系统调用延迟模拟已启动，PID: $!"

# 4. 上下文切换频繁模拟
echo "启动上下文切换频繁模拟..."
stress --cpu 4 --timeout 3600 &
echo "上下文切换频繁模拟已启动，PID: $!"

echo "故障模拟已启动"
echo "使用ftrace和perf进行故障排查练习"
echo "示例命令:"
echo "  perf top"
echo "  perf record -g -a -- sleep 10"
echo "  perf report"
echo "  trace-cmd record -p function_graph -g sys_write"
echo "  trace-cmd report"    