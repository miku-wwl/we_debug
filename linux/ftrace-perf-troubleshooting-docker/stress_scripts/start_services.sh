#!/bin/bash
# 启动服务脚本 - 根据参数启动不同的压力测试

case "$1" in
    ftrace-perf)
        /troubleshoot/stress_scripts/ftrace_perf_stress.sh
        ;;
    none)
        echo "未启动任何压力测试。使用以下命令启动ftrace/perf测试："
        echo "/troubleshoot/stress_scripts/start_services.sh ftrace-perf"
        ;;
    *)
        echo "未知参数: $1"
        echo "可用参数: ftrace-perf, none"
        exit 1
        ;;
esac

# 保持容器运行
tail -f /dev/null    