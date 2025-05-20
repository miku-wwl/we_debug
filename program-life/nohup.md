$ java -jar xx.jar &
$ nohup java -jar xx.jar &>stdout.log


### 案例背景
假设你开发了一个实时消息推送服务 `push-service.jar`，需要在 Linux 服务器上持续运行。现在要选择合适的命令启动它。

### 1. `java -jar xx.jar &`
这个命令会将程序放入后台执行，但存在以下问题：

**示例操作**：
```bash
$ java -jar push-service.jar &
[1] 25678  # 后台进程ID
$ jobs
[1]+  Running                 java -jar push-service.jar &
```

**特点**：
- **会话依赖**：进程与当前终端会话绑定。如果用户退出 SSH 会话，进程会收到 SIGHUP 信号而终止。
- **输出混乱**：程序输出会直接打印到终端，即使在后台运行。如果多人共用服务器，可能干扰其他用户。
- **关闭方式**：直接关闭终端或执行 `exit` 会终止进程。

**问题场景**：
当你深夜 SSH 登录服务器启动服务后，早上发现服务莫名停止——这是因为你关闭了终端，触发了 SIGHUP 信号。


### 2. `nohup java -jar xx.jar &>stdout.log &`
这个命令解决了上述问题：

**示例操作**：
```bash
$ nohup java -jar push-service.jar &>stdout.log &
[1] 25679
$ nohup: ignoring input and appending output to 'stdout.log'
```

**特点**：
- **会话独立**：`nohup` 使进程忽略 SIGHUP 信号，即使关闭终端也会继续运行。
- **输出重定向**：`&>stdout.log` 将标准输出和错误输出都定向到 `stdout.log` 文件，避免干扰终端。
- **持久化运行**：适合需要长期稳定运行的服务（如 Web 服务器、定时任务）。

**验证方法**：
```bash
# 退出会话后重新登录，检查进程是否存在
$ ps -ef | grep push-service
user     25679  1  0 10:00 ?        00:00:02 java -jar push-service.jar
```


### 关键区别对比表
| 特性               | `java -jar &`               | `nohup java -jar &>log &`    |
|--------------------|-----------------------------|-----------------------------|
| 会话依赖           | 是（关闭终端则终止）        | 否（独立于会话）            |
| 输出处理           | 混杂在当前终端              | 定向到指定日志文件          |
| 进程ID查看         | 通过 `jobs` 命令            | 通过 `ps` 或 `pgrep` 命令   |
| 典型应用场景       | 临时测试、可中断的后台任务  | 生产环境服务、需要7×24运行  |



**查看日志实时更新**：
   ```bash
   tail -f stdout.log  # 实时监控日志
   ```

**正确停止服务**：
   ```bash
   pkill -f push-service.jar  # 通过进程名终止
   ```

- **使用 `java -jar &`**：适合临时测试，不需要长期运行的场景。
- **使用 `nohup ... &>log &`**：适合生产环境，确保服务持续运行并集中管理日志。