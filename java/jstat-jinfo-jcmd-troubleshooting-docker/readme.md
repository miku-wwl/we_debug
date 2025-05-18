docker build -t jstat-jinfo-jcmd-troubleshooting .

# GC压力测试
docker run -it --name troubleshoot-java-gc jstat-jinfo-jcmd-troubleshooting /troubleshoot/stress_scripts/start_services.sh gc-stress


```
[arthas@3762]$ memory
Memory                                                                  used                    total                   max                     usage
heap                                                                    245M                    382M                    968M                    25.31%
g1_eden_space                                                           1M                      46M                     -1                      2.17%
g1_old_gen                                                              241M                    333M                    968M                    24.90%
g1_survivor_space                                                       3M                      3M                      -1                      100.00%
nonheap                                                                 41M                     44M                     -1                      93.13%
codeheap_'non-nmethods'                                                 1M                      2M                      5M                      30.58%
metaspace                                                               30M                     31M                     -1                      98.93%
codeheap_'profiled_nmethods'                                            4M                      4M                      117M                    3.84%
compressed_class_space                                                  3M                      3M                      1024M                   0.35%
codeheap_'non-profiled_nmethods'                                        952K                    2496K                   120036K                 0.79%
mapped                                                                  0K                      0K                      -                       0.00%
direct                                                                  4M                      4M                      -                       100.00%
mapped - 'non-volatile memory'                                          0K                      0K                      -                       0.00%
[arthas@3762]$ memory
Memory                                                                  used                    total                   max                     usage
heap                                                                    251M                    382M                    968M                    25.93%
g1_eden_space                                                           7M                      46M                     -1                      15.22%
g1_old_gen                                                              241M                    333M                    968M                    24.90%
g1_survivor_space                                                       3M                      3M                      -1                      100.00%
nonheap                                                                 41M                     44M                     -1                      93.21%
codeheap_'non-nmethods'                                                 1M                      2M                      5M                      30.58%
metaspace                                                               30M                     31M                     -1                      98.82%
codeheap_'profiled_nmethods'                                            4M                      4M                      117M                    3.89%
compressed_class_space                                                  3M                      3M                      1024M                   0.35%
codeheap_'non-profiled_nmethods'                                        957K                    2496K                   120036K                 0.80%
mapped                                                                  0K                      0K                      -                       0.00%
direct                                                                  4M                      4M                      -                       100.00%
mapped - 'non-volatile memory'                                          0K                      0K                      -                       0.00%
[arthas@3762]$ memory
Memory                                                                  used                    total                   max                     usage
heap                                                                    259M                    382M                    968M                    26.76%
g1_eden_space                                                           14M                     46M                     -1                      30.43%
g1_old_gen                                                              242M                    333M                    968M                    25.00%
g1_survivor_space                                                       3M                      3M                      -1                      100.00%
nonheap                                                                 41M                     44M                     -1                      93.33%
codeheap_'non-nmethods'                                                 1M                      2M                      5M                      30.58%
metaspace                                                               30M                     31M                     -1                      98.75%
codeheap_'profiled_nmethods'                                            4M                      4M                      117M                    3.95%
compressed_class_space                                                  3M                      3M                      1024M                   0.35%
codeheap_'non-profiled_nmethods'                                        961K                    2496K                   120036K                 0.80%
mapped                                                                  0K                      0K                      -                       0.00%
direct                                                                  4M                      4M                      -                       100.00%
mapped - 'non-volatile memory'                                          0K                      0K                      -                       0.00%
[arthas@3762]$ memory
Memory                                                                  used                    total                   max                     usage
heap                                                                    264M                    382M                    968M                    27.27%
g1_eden_space                                                           19M                     46M                     -1                      41.30%
g1_old_gen                                                              242M                    333M                    968M                    25.00%
g1_survivor_space                                                       3M                      3M                      -1                      100.00%
nonheap                                                                 41M                     44M                     -1                      93.51%
codeheap_'non-nmethods'                                                 1M                      2M                      5M                      30.58%
metaspace                                                               30M                     31M                     -1                      98.82%
codeheap_'profiled_nmethods'                                            4M                      4M                      117M                    4.00%
compressed_class_space                                                  3M                      3M                      1024M                   0.35%
codeheap_'non-profiled_nmethods'                                        965K                    2496K                   120036K                 0.80%
mapped                                                                  0K                      0K                      -                       0.00%
direct                                                                  4M                      4M                      -                       100.00%
mapped - 'non-volatile memory'                                          0K                      0K                      -                       0.00%


heapdump arthas-output/dump.hprof

![alt text](image.png)
![alt text](image-1.png)
```


# 堆内存溢出
docker run -it --name troubleshoot-java-heap jstat-jinfo-jcmd-troubleshooting /troubleshoot/stress_scripts/start_services.sh heap-oom

# 元空间溢出
docker run -it --name troubleshoot-java-metaspace jstat-jinfo-jcmd-troubleshooting /troubleshoot/stress_scripts/start_services.sh metaspace-oom

# CPU密集型
docker run -it --name troubleshoot-java-cpu jstat-jinfo-jcmd-troubleshooting /troubleshoot/stress_scripts/start_services.sh cpu

# 所有故障
docker run -it --name troubleshoot-java-all jstat-jinfo-jcmd-troubleshooting /troubleshoot/stress_scripts/start_services.sh all

docker exec -it troubleshoot-java-all bash


# 查看Java进程ID
jps

# 监控GC活动（每1秒更新一次）
jstat -gc <PID> 1000

# 监控类加载统计信息
jstat -class <PID>

# 监控编译统计信息
jstat -compiler <PID>

# 监控GC详细信息
jstat -gccapacity <PID>
jstat -gcutil <PID>


# 查看所有JVM参数
jinfo <PID>

# 查看特定JVM参数
jinfo -flag MaxHeapSize <PID>
jinfo -flag UseG1GC <PID>

# 查看所有系统属性
jinfo -sysprops <PID>


# 查看支持的命令
jcmd <PID> help

# 生成堆转储文件
jcmd <PID> GC.heap_dump heapdump.hprof

# 触发GC
jcmd <PID> GC.run

# 查看线程堆栈
jcmd <PID> Thread.print > thread_dump.txt

# 查看JVM概要信息
jcmd <PID> VM.summary

# 查看JVM命令行标志
jcmd <PID> VM.flags

# 查看GC统计信息
jcmd <PID> GC.class_histogram

