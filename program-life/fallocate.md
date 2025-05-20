fallocate不停机高效保留最后500M日志

```bash
# 计算stdout.log保留500M，需要删除的大小，以磁盘块大小为单位
rmlen=$(( ($(stat -c %s stdout.log) - $(numfmt --from=auto 500Mi)) / 4096 * 4096 ))

# 从文件开始处，删除$rmlen大小
fallocate -o 0 -l $rmlen -c stdout.log
```