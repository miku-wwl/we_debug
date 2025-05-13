#!/bin/bash
# 启动服务脚本 - 根据参数启动不同的压力测试

case "$1" in
    cpu)
        /troubleshoot/stress_scripts/cpu_stress.sh
        ;;
    memory)
        /troubleshoot/stress_scripts/memory_stress.sh
        ;;
    disk)
        /troubleshoot/stress_scripts/disk_stress.sh
        ;;
    network)
        /troubleshoot/stress_scripts/network_stress.sh
        ;;
    all)
        /troubleshoot/stress_scripts/cpu_stress.sh
        /troubleshoot/stress_scripts/memory_stress.sh
        /troubleshoot/stress_scripts/disk_stress.sh
        /troubleshoot/stress_scripts/network_stress.sh
        ;;
    none)
        echo "未启动任何压力测试。使用以下命令启动特定测试："
        echo "/troubleshoot/stress_scripts/start_services.sh [cpu|memory|disk|network|all]"
        ;;
    *)
        echo "未知参数: $1"
        echo "可用参数: cpu, memory, disk, network, all, none"
        exit 1
        ;;
esac

# 保持容器运行
tail -f /dev/null    