docker build -t jstack-jmap-troubleshooting .


# 线程死锁
docker run -it --name troubleshoot-java-deadlock jstack-jmap-troubleshooting /troubleshoot/stress_scripts/start_services.sh deadlock

``` java
[arthas@6]$ thread -b
"Thread-0" Id=12 BLOCKED on java.lang.Object@3f2330ff owned by "Thread-1" Id=13
    at app//DeadlockExample.lambda$main$0(DeadlockExample.java:12)
    -  blocked on java.lang.Object@3f2330ff
    -  locked java.lang.Object@39baefc1 <---- but blocks 1 other threads!
    at app//DeadlockExample$$Lambda$1/0x0000000800c00a08.run(Unknown Source)
    at java.base@17.0.2/java.lang.Thread.run(Thread.java:833)


[arthas@6]$ jad DeadlockExample

ClassLoader:
+-jdk.internal.loader.ClassLoaders$AppClassLoader@42110406
  +-jdk.internal.loader.ClassLoaders$PlatformClassLoader@273f6b01

Location:
/troubleshoot/bin/

       /*
        * Decompiled with CFR.
        */
       public class DeadlockExample {
           private static final Object resource1 = new Object();
           private static final Object resource2 = new Object();

           public static void main(String[] stringArray) {
               Thread thread = new Thread(() -> {
/* 7*/             Object object = resource1;
                   synchronized (object) {
/* 8*/                 System.out.println("Thread 1: Holding resource 1...");
                       try {
/* 9*/                     Thread.sleep(100L);
                       }
                       catch (InterruptedException interruptedException) {
                           // empty catch block
                       }
/*10*/                 System.out.println("Thread 1: Waiting for resource 2..."                                                                                                       );
/*11*/                 Object object2 = resource2;
                       synchronized (object2) {
/*12*/                     System.out.println("Thread 1: Holding resource 1 and                                                                                                        2...");
                       }
                   }
               });
               Thread thread2 = new Thread(() -> {
/*18*/             Object object = resource2;
                   synchronized (object) {
/*19*/                 System.out.println("Thread 2: Holding resource 2...");
                       try {
/*20*/                     Thread.sleep(100L);
                       }
                       catch (InterruptedException interruptedException) {
                           // empty catch block
                       }
/*21*/                 System.out.println("Thread 2: Waiting for resource 1..."                                                                                                       );
/*22*/                 Object object2 = resource1;
                       synchronized (object2) {
/*23*/                     System.out.println("Thread 2: Holding resource 2 and                                                                                                        1...");
                       }
                   }
               });
/*28*/         thread.start();
/*29*/         thread2.start();
               try {
/*32*/             thread.join();
/*33*/             thread2.join();
               }
               catch (InterruptedException interruptedException) {
/*35*/             interruptedException.printStackTrace();
               }
           }
       }
破坏12行死锁条件；
```





# CPU密集型
docker run -it --rm --name troubleshoot-java-cpu jstack-jmap-troubleshooting /troubleshoot/stress_scripts/start_services.sh cpu

``` java
[arthas@6]$ thread -n 4
"CPU-Intensive-Thread-0" Id=12 cpuUsage=52.27% deltaTime=109ms time=25870ms RUNNABLE
    at app//CPUIntensiveExample.lambda$main$0(CPUIntensiveExample.java:8)
    at app//CPUIntensiveExample$$Lambda$1/0x0000000800c00a08.run(Unknown Source)
    at java.base@17.0.2/java.lang.Thread.run(Thread.java:833)


"CPU-Intensive-Thread-1" Id=13 cpuUsage=47.68% deltaTime=100ms time=26032ms RUNNABLE
    at app//CPUIntensiveExample.lambda$main$0(CPUIntensiveExample.java:8)
    at app//CPUIntensiveExample$$Lambda$1/0x0000000800c00a08.run(Unknown Source)
    at java.base@17.0.2/java.lang.Thread.run(Thread.java:833)


"CPU-Intensive-Thread-2" Id=14 cpuUsage=46.82% deltaTime=98ms time=26364ms RUNNABLE
    at app//CPUIntensiveExample.lambda$main$0(CPUIntensiveExample.java:8)
    at app//CPUIntensiveExample$$Lambda$1/0x0000000800c00a08.run(Unknown Source)
    at java.base@17.0.2/java.lang.Thread.run(Thread.java:833)


"CPU-Intensive-Thread-3" Id=15 cpuUsage=45.94% deltaTime=96ms time=25954ms RUNNABLE
    at app//CPUIntensiveExample.lambda$main$0(CPUIntensiveExample.java:8)
    at app//CPUIntensiveExample$$Lambda$1/0x0000000800c00a08.run(Unknown Source)
    at java.base@17.0.2/java.lang.Thread.run(Thread.java:833)


[arthas@6]$ jad CPUIntensiveExample

ClassLoader:
+-jdk.internal.loader.ClassLoaders$AppClassLoader@42110406
  +-jdk.internal.loader.ClassLoaders$PlatformClassLoader@273f6b01

Location:
/troubleshoot/bin/

       /*
        * Decompiled with CFR.
        */
       public class CPUIntensiveExample {
           public static void main(String[] stringArray) {
/* 4*/         for (int i = 0; i < 4; ++i) {
                   Thread thread = new Thread(() -> {
                       while (true) {
/* 8*/                     Math.pow(999.0, 999.0);
                       }
                   });
/*11*/             thread.setName("CPU-Intensive-Thread-" + i);
/*12*/             thread.start();
               }
               try {
/*17*/             Thread.sleep(Long.MAX_VALUE);
               }
               catch (InterruptedException interruptedException) {
/*19*/             interruptedException.printStackTrace();
               }
           }
       }

Affect(row-cnt:1) cost in 2059 ms.


修改 while (true) {Math.pow(999.0, 999.0);} 这一行
```


# 内存泄漏
docker run -it --rm --name troubleshoot-java-memory jstack-jmap-troubleshooting /troubleshoot/stress_scripts/start_services.sh memory

``` java
[arthas@6]$ memory
Memory                                                                            used                       total                      max                         usage
heap                                                                              44M                        92M                        256M                        17.40%
g1_eden_space                                                                     1M                         16M                        -1                          6.25%
g1_old_gen                                                                        40M                        73M                        256M                        15.84%
g1_survivor_space                                                                 3M                         3M                         -1                          100.00%
nonheap                                                                           28M                        31M                        -1                          89.63%
codeheap_'non-nmethods'                                                           1M                         2M                         5M                          22.62%
metaspace                                                                         21M                        21M                        -1                          99.23%
codeheap_'profiled_nmethods'                                                      3M                         3M                         117M                        2.59%
compressed_class_space                                                            2M                         2M                         1024M                       0.24%
codeheap_'non-profiled_nmethods'                                                  590K                       2496K                      120036K                     0.49%
mapped                                                                            0K                         0K                         -                           0.00%
direct                                                                            4M                         4M                         -                           100.00%
mapped - 'non-volatile memory'                                                    0K                         0K                         -                           0.00%


[arthas@6]$ memory
Memory                                                                            used                       total                      max                         usage
heap                                                                              146M                       230M                       256M                        57.36%
g1_eden_space                                                                     1M                         19M                        -1                          5.26%
g1_old_gen                                                                        143M                       209M                       256M                        56.19%
g1_survivor_space                                                                 2M                         2M                         -1                          100.00%
nonheap                                                                           30M                        33M                        -1                          90.47%
codeheap_'non-nmethods'                                                           1M                         2M                         5M                          22.77%
metaspace                                                                         21M                        21M                        -1                          98.98%
codeheap_'profiled_nmethods'                                                      4M                         4M                         117M                        3.75%
compressed_class_space                                                            2M                         2M                         1024M                       0.25%
codeheap_'non-profiled_nmethods'                                                  766K                       2496K                      120036K                     0.64%
mapped                                                                            0K                         0K                         -                           0.00%
direct                                                                            4M                         4M                         -                           100.00%
mapped - 'non-volatile memory'                                                    0K                         0K                         -                           0.00%

heapdump arthas-output/dump.hprof

使用heapdump命令生成堆的全量内存信息文件，如heapdump arthas-output/dump.hprof。这一步骤对于精确地定位内存泄漏对象至关重要。多次执行此操作并在不同时间点生成堆转储文件，以便后续对比分析。


利用诸如Eclipse Memory Analyzer (MAT)等工具分析之前生成的.hprof文件。在MAT中，你可以查看对象的数量、它们的引用关系以及占用的内存大小，通过对比不同时间点的堆转储文件，寻找那些数量显著增加或内存占用持续增长的对象，这些很可能是导致内存泄漏的根源。

```



# 线程阻塞
docker run -it --name troubleshoot-java-blocked jstack-jmap-troubleshooting /troubleshoot/stress_scripts/start_services.sh blocked

# 所有故障
docker run -it --name troubleshoot-java-all jstack-jmap-troubleshooting /troubleshoot/stress_scripts/start_services.sh all


docker exec -it troubleshoot-java-all bash


# 查看所有Java进程
jps

# 获取线程堆栈信息
jstack <PID>  # 替换<PID>为Java进程ID

# 连续输出线程堆栈，用于分析CPU高的问题
while true; do jstack <PID> > stack_$(date +%s).txt; sleep 1; done

# 分析死锁
jstack <PID> | grep -A 20 "BLOCKED"
jstack <PID> | grep "deadlock" -A 50 -B 50



# 查看堆内存使用情况
jmap -heap <PID>  # 替换<PID>为Java进程ID

# 生成堆转储文件
jmap -dump:format=b,file=heapdump.hprof <PID>

# 查看对象统计信息
jmap -histo <PID> > histo.txt

# 对于内存泄漏，定期生成堆转储，比较差异
for i in {1..5}; do jmap -dump:format=b,file=heapdump_$i.hprof <PID>; sleep 60; done



# 分析CPU高的线程
top -Hp <PID>  # 找出CPU占用高的线程ID
printf "%x\n" <TID>  # 将线程ID转换为16进制
jstack <PID> | grep <hex_tid> -A 30  # 查找对应线程堆栈

# 使用jhat分析堆转储文件
jhat heapdump.hprof