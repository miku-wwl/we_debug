docker build -t ftrace-perf-troubleshooting .

docker run -it --name troubleshoot-ftrace-perf \
  --privileged \
  -v /sys/kernel/debug:/sys/kernel/debug \
  ftrace-perf-troubleshooting /troubleshoot/stress_scripts/start_services.sh ftrace-perf

docker exec -it troubleshoot-ftrace-perf bash


# 实时监控CPU热点函数
perf top

# 记录一段时间内的CPU调用图
perf record -g -a -- sleep 10
perf report

# 分析内存分配热点
perf record -e malloc -g -p <PID> -- sleep 10
perf report

# 分析上下文切换
perf record -e sched:sched_switch -a -- sleep 10
perf report

# 分析系统调用延迟
perf record -e syscalls:sys_enter_write -e syscalls:sys_exit_write -a -- sleep 10
perf script | ./stackcollapse-perf.pl | ./flamegraph.pl > write_flamegraph.svg



# 初始化ftrace
mount -t debugfs none /sys/kernel/debug

# 跟踪特定函数
echo function_graph > /sys/kernel/debug/tracing/current_tracer
echo sys_write > /sys/kernel/debug/tracing/set_graph_function
echo 1 > /sys/kernel/debug/tracing/tracing_on
sleep 10
echo 0 > /sys/kernel/debug/tracing/tracing_on
cat /sys/kernel/debug/tracing/trace > ftrace_sys_write.txt

# 使用trace-cmd前端工具
trace-cmd record -p function_graph -g sys_write -a sleep 10
trace-cmd report

# 跟踪上下文切换
trace-cmd record -e sched_switch -a sleep 10
trace-cmd report
