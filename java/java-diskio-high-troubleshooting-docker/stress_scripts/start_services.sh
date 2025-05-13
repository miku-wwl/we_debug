#!/bin/bash
# 启动服务脚本 - 根据参数启动不同的磁盘I/O高使用率场景

case "$1" in
    file-write)
        echo "启动文件写入高峰示例..."
        java -cp /troubleshoot/bin FileWriteSpikeExample > /tmp/file_write.log 2>&1 &
        echo "文件写入高峰示例已启动，PID: $!"
        ;;
    random-access)
        echo "启动随机文件访问示例..."
        java -cp /troubleshoot/bin RandomFileAccessExample > /tmp/random_access.log 2>&1 &
        echo "随机文件访问示例已启动，PID: $!"
        ;;
    logging)
        echo "启动日志写入高峰示例..."
        java -cp /troubleshoot/bin LoggingSpikeExample > /tmp/logging.log 2>&1 &
        echo "日志写入高峰示例已启动，PID: $!"
        ;;
    file-copy)
        echo "启动大文件复制示例..."
        java -cp /troubleshoot/bin FileCopyExample > /tmp/file_copy.log 2>&1 &
        echo "大文件复制示例已启动，PID: $!"
        ;;
    file-locking)
        echo "启动文件锁争用示例..."
        java -cp /troubleshoot/bin NioFileLockingExample > /tmp/file_locking.log 2>&1 &
        echo "文件锁争用示例已启动，PID: $!"
        ;;
    all)
        echo "启动所有磁盘I/O高使用率示例..."
        java -cp /troubleshoot/bin FileWriteSpikeExample > /tmp/file_write.log 2>&1 &
        echo "文件写入高峰示例已启动，PID: $!"
        sleep 1
        java -cp /troubleshoot/bin RandomFileAccessExample > /tmp/random_access.log 2>&1 &
        echo "随机文件访问示例已启动，PID: $!"
        sleep 1
        java -cp /troubleshoot/bin LoggingSpikeExample > /tmp/logging.log 2>&1 &
        echo "日志写入高峰示例已启动，PID: $!"
        sleep 1
        java -cp /troubleshoot/bin FileCopyExample > /tmp/file_copy.log 2>&1 &
        echo "大文件复制示例已启动，PID: $!"
        sleep 1
        java -cp /troubleshoot/bin NioFileLockingExample > /tmp/file_locking.log 2>&1 &
        echo "文件锁争用示例已启动，PID: $!"
        ;;
    none)
        echo "未启动任何磁盘I/O高使用率示例。使用以下命令启动特定故障："
        echo "/troubleshoot/stress_scripts/start_services.sh [file-write|random-access|logging|file-copy|file-locking|all]"
        ;;
    *)
        echo "未知参数: $1"
        echo "可用参数: file-write, random-access, logging, file-copy, file-locking, all, none"
        exit 1
        ;;
esac

# 保持容器运行
tail -f /dev/null    