### 查看进程打开文件的方法
1. **使用 `ls -l /proc/PID/fd` 命令**
   - 此命令用于查看某个进程（PID）打开的所有文件。
   - 示例：`ls -l /proc/1690/fd | grep log`
2. **使用 `lsof -c 进程名` 命令**
   - 该命令能够查看指定进程打开的所有文件。
   - 示例：`lsof -c java | grep log`