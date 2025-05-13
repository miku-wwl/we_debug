#!/bin/bash
# 启动服务脚本 - 根据参数启动不同的压力测试

case "$1" in
    bcc-bpftrace)
        /troubleshoot/stress_scripts/bcc_bpftrace_stress.sh
        ;;
    none)
        echo "未启动任何压力测试。使用以下命令启动BCC/bpftrace测试："
        echo "/troubleshoot/stress_scripts/start_services.sh bcc-bpftrace"
        ;;
    *)
        echo "未知参数: $1"
        echo "可用参数: bcc-bpftrace, none"
        exit 1
        ;;
esac

# 保持容器运行
tail -f /dev/null    