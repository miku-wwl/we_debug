# CPU问题
docker run -it --name troubleshoot-cpu troubleshooting-practice /troubleshoot/stress_scripts/start_services.sh cpu

# 内存问题
docker run -it --name troubleshoot-memory troubleshooting-practice /troubleshoot/stress_scripts/start_services.sh memory

# 磁盘问题
docker run -it --name troubleshoot-disk troubleshooting-practice /troubleshoot/stress_scripts/start_services.sh disk

# 网络问题
docker run -it --name troubleshoot-network troubleshooting-practice /troubleshoot/stress_scripts/start_services.sh network

# 所有资源问题
docker run -it --name troubleshoot-all troubleshooting-practice /troubleshoot/stress_scripts/start_services.sh all

docker exec -it troubleshoot-cpu bash

# CPU问题排查
top
vmstat 1
sar -u 1

``` shell

[root@harbor ~]# docker exec -it troubleshoot-cpu bash
root@6e3c4e893d36:/troubleshoot# top
top - 12:17:01 up 24 min,  0 users,  load average: 3.21, 1.08, 0.52
Tasks:   9 total,   5 running,   4 sleeping,   0 stopped,   0 zombie
%Cpu(s): 93.0 us,  6.9 sy,  0.0 ni,  0.0 id,  0.0 wa,  0.0 hi,  0.2 si,  0.0 st
MiB Mem :   5644.6 total,   1822.2 free,   1809.5 used,   2012.9 buff/cache
MiB Swap:   4096.0 total,   4096.0 free,      0.0 used.   3504.9 avail Mem

   PID USER      PR  NI    VIRT    RES    SHR S  %CPU  %MEM     TIME+ COMMAND
    10 root      20   0    3692    108      0 R  45.5   0.0   0:30.65 stress
    12 root      20   0    3692    108      0 R  43.5   0.0   0:29.74 stress
    11 root      20   0    3692    108      0 R  40.9   0.0   0:30.79 stress
     9 root      20   0    3692    108      0 R  39.9   0.0   0:30.36 stress
     1 root      20   0    4348   1548   1304 S   0.0   0.0   0:00.02 start_services.
     7 root      20   0    3692    604    504 S   0.0   0.0   0:00.00 stress
     8 root      20   0    2808    504    412 S   0.0   0.0   0:00.00 tail
    13 root      20   0    4612   2280   1708 S   0.0   0.0   0:00.02 bash
    20 root      20   0    7300   1988   1376 R   0.0   0.0   0:00.02 top

root@6e3c4e893d36:/troubleshoot# vmstat 1
procs -----------memory---------- ---swap-- -----io---- -system-- ------cpu-----
 r  b   swpd   free   buff  cache   si   so    bi    bo   in   cs us sy id wa st
 6  0      0 1867592   1052 2060188    0    0   454   332  458  702  8  4 88  0  0
18  0      0 1851796   1052 2060188    0    0     0    31 2523 2365 94  6  0  0  0
 6  0      0 1866980   1052 2060196    0    0   288   169 2532 2191 90 11  0  0  0
 6  0      0 1866820   1052 2060196    0    0     0     0 2157  858 99  1  0  0  0
 4  0      0 1866828   1052 2060196    0    0     0     0 2168  836 99  1  0  0  0
 4  0      0 1866952   1052 2060196    0    0     0     0 2185  816 100  0  0  0  0
 5  0      0 1866780   1052 2060196    0    0     0   498 2194  849 100  1  0  0  0
 4  0      0 1867052   1052 2060196    0    0     0     0 2196  814 100  0  0  0  0
 5  0      0 1866788   1052 2060196    0    0     0     0 2183  863 99  1  0  0  0
 5  0      0 1866728   1052 2060196    0    0     0     0 2181  861 98  2  0  0  0
 4  0      0 1866788   1052 2060196    0    0     0     0 2189  849 99  1  0  0  0
 4  0      0 1866888   1052 2060196    0    0     0     0 2168  833 100  0  0  0  0
 4  0      0 1866676   1052 2060196    0    0     0     0 2174 1035 99  1  0  0  0
^C
root@6e3c4e893d36:/troubleshoot# sar -u 1
Linux 3.10.0-1160.119.1.el7.x86_64 (6e3c4e893d36)       05/13/25        _x86_64_        (2 CPU)

12:17:27        CPU     %user     %nice   %system   %iowait    %steal     %idle
12:17:28        all    100.00      0.00      0.00      0.00      0.00      0.00
12:17:29        all     99.50      0.00      0.50      0.00      0.00      0.00
12:17:30        all     99.50      0.00      0.50      0.00      0.00      0.00
12:17:31        all     97.51      0.00      2.49      0.00      0.00      0.00
12:17:32        all     99.49      0.00      0.51      0.00      0.00      0.00
12:17:33        all     99.50      0.00      0.50      0.00      0.00      0.00
12:17:34        all    100.00      0.00      0.00      0.00      0.00      0.00
12:17:35        all     99.00      0.00      1.00      0.00      0.00      0.00
12:17:36        all     99.50      0.00      0.50      0.00      0.00      0.00
^C
Average:        all     99.33      0.00      0.67      0.00      0.00      0.00
```



# 内存问题排查
free -h
vmstat 1
slabtop

``` shell
root@28b07e155907:/troubleshoot# free -h
               total        used        free      shared  buff/cache   available
Mem:           5.5Gi       2.8Gi       745Mi        65Mi       2.0Gi       2.4Gi
Swap:          4.0Gi          0B       4.0Gi
root@28b07e155907:/troubleshoot# vmstat 1
procs -----------memory---------- ---swap-- -----io---- -system-- ------cpu-----
 r  b   swpd   free   buff  cache   si   so    bi    bo   in   cs us sy id wa st
 4  0      0 761168   1052 2116400    0    0   418   295  503  679 14  4 82  0  0
 4  0      0 761044   1052 2116400    0    0     0     0 1404  776 51  1 48  0  0
 5  0      0 761168   1052 2116404    0    0     0     0 1409  740 52  0 48  0  0
 1  0      0 761292   1052 2116404    0    0     0     0 1423  855 52  1 47  0  0
 1  0      0 761540   1052 2116404    0    0     0     0 1390  711 52  1 47  1  0
 1  0      0 761540   1052 2116404    0    0     0     0 1327  677 51  1 48  0  0
 1  0      0 761476   1052 2116404    0    0     0     0 1337  652 51  1 49  0  0
 1  0      0 761352   1052 2116404    0    0     0     0 1336  686 51  1 48  0  0
 2  0      0 761476   1052 2116404    0    0     0     0 1367  667 51  0 49  0  0
^C
root@28b07e155907:/troubleshoot# slabtop
 Active / Total Objects (% used)    : 1477433 / 1536789 (96.1%)
 Active / Total Slabs (% used)      : 24183 / 24183 (100.0%)
 Active / Total Caches (% used)     : 91 / 122 (74.6%)
 Active / Total Size (% used)       : 260623.52K / 270049.21K (96.5%)
 Minimum / Average / Maximum Object : 0.01K / 0.17K / 8.00K

  OBJS ACTIVE  USE OBJ SIZE  SLABS OBJ/SLAB CACHE SIZE NAME
 96768  96768 100%    0.01K    189      512       756K kmalloc-8
     0      0   0%    0.01K      0      512         0K dma-kmalloc-8
121600 120318  98%    0.02K    475      256      1900K kmalloc-16
     0      0   0%    0.02K      0      256         0K dma-kmalloc-16
137360 137360 100%    0.02K    808      170      3232K fsnotify_mark_connector
164096 147624  89%    0.03K   1282      128      5128K kmalloc-32
     0      0   0%    0.03K      0      128         0K dma-kmalloc-32
121380 112208  92%    0.04K   1190      102      4760K selinux_inode_security
 11220  11220 100%    0.05K    132       85       528K shared_policy_node
   219    219 100%    0.05K      3       73 12K avc_xperms_node256
     0      0   0%    0.23K      0       70         0K cfq_queue
 54400  53770  98%    0.12K    800       68      6400K kernfs_node_cache
   136    136 100%    0.47K      2       68        64K xfs_da_state
     0      0   0%    0.48K      0       67 0K user_namespacec-512
   132    132 100%    0.24K      2       66 32K posix_timers_cache
     0      0   0%    0.48K      0       66         0K xfs_dquot
   192    192 100%    0.25K      3       64        48K kmem_cache
   192    192 100%    0.06K      3       64 12K kmem_cache_node2048
122560 121897  99%    0.06K   1915       64      7660K kmalloc-64
  7424   7056  95%    0.12K    116       64 928 13632K kmalloc-128
 41024  40572  98%    0.25K    641       64     10256K kmalloc-256
 33600  31624  94%    0.50K    525       64     16800K kmalloc-512
     0      0   0%    0.06K      0       64         0K dma-kmalloc-64
     0      0   0%    0.12K      0       64         0K dma-kmalloc-128
     0      0   0%    0.25K      0       64         0K dma-kmalloc-256
    64     64 100%    0.50K      1       64        32K dma-kmalloc-512
   896    896 100%    0.12K     14       64       112K pid
   384    384 100%    0.06K      6       64        24K fs_cache
     0      0   0%    0.12K      0       64         0K iint_cache
   128    128 100%    0.25K      2       64        32K tw_sock_TCP
     0      0   0%    0.25K      0       64         0K dquot
     0      0   0%    0.25K      0       64         0K tw_sock_TCPv6
   186    186 100%    0.26K      3       62        48K numa_policy
     0      0   0%    0.52K      0       62         0K xfs_dqtrx
     0      0   0%    0.13K      0       60         0K dm_rq_target_io
 18312  18275  99%    0.57K    327       56     10464K radix_tree_node
 11984  11110  92%    0.07K    214       56       856K avc_node
     0      0   0%    0.14K      0       56         0K flow_cache
    56     56 100%    0.56K      1       56        32K kioctx
 34540  34370  99%    0.58K    628       55     20096K inode_cache
   106    106 100%    0.59K      2       53        64K hugetlbfs_inode_cache
     0      0   0%    0.30K      0       52         0K bsg_cmd
   357    357 100%    0.62K      7       51       224K files_cache
   102    102 100%    0.16K      2       51        16K sigqueue
   255    255 100%    0.08K      5       51        20K Acpi-State
  6834   6703  98%    0.62K    134       51      4288K sock_inode_cache
    51     51 100%    0.62K      1       51        32K dio
   255    255 100%    0.31K      5       51        80K bio-2
    51     51 100%    0.62K      1       51        32K rpc_inode_cache
   102    102 100%    0.31K      2       51        32K nf_conntrack_1


```

# 磁盘问题排查
df -h
iostat -xm 1
iotop

# 网络问题排查
nicstat
iftop
sar -n DEV 1
dstat -n


``` shell
[root@harbor ~]# docker exec -it troubleshoot-network  bash
root@22edad7d6847:/troubleshoot# nicstat
    Time      Int   rKB/s   wKB/s   rPk/s   wPk/s    rAvs    wAvs %Util    Sat
12:33:15     eth0    0.00    0.00    0.00    0.00   82.00    0.00  0.00   0.00
12:33:15       lo    0.88    0.88    8.86    8.86   102.0   102.0  0.00   0.00
root@22edad7d6847:/troubleshoot# nicstat
    Time      Int   rKB/s   wKB/s   rPk/s   wPk/s    rAvs    wAvs %Util    Sat
12:33:17     eth0    0.00    0.00    0.00    0.00   82.00    0.00  0.00   0.00
12:33:17       lo    0.96    0.96    9.65    9.65   102.0   102.0  0.00   0.00
root@22edad7d6847:/troubleshoot# iftop
interface: eth0
IP address is: 172.17.0.4
MAC address is: 02:42:ac:11:00:04
root@22edad7d6847:/troubleshoot# sar -n DEV 1
Linux 3.10.0-1160.119.1.el7.x86_64 (22edad7d6847)       05/13/25        _x86_64_        (2 CPU)

12:33:34        IFACE   rxpck/s   txpck/s    rxkB/s    txkB/s   rxcmp/s   txcmp/s  rxmcst/s   %ifutil
12:33:35         eth0      0.00      0.00      0.00      0.00      0.00      0.00      0.00      0.00
12:33:35           lo   1273.27   1273.27    126.94    126.94      0.00      0.00      0.00      0.00

12:33:35        IFACE   rxpck/s   txpck/s    rxkB/s    txkB/s   rxcmp/s   txcmp/s  rxmcst/s   %ifutil
12:33:36         eth0      0.00      0.00      0.00      0.00      0.00      0.00      0.00      0.00
12:33:36           lo   1287.00   1287.00    128.26    128.26      0.00      0.00      0.00      0.00

12:33:36        IFACE   rxpck/s   txpck/s    rxkB/s    txkB/s   rxcmp/s   txcmp/s  rxmcst/s   %ifutil
12:33:37         eth0      0.00      0.00      0.00      0.00      0.00      0.00      0.00      0.00
12:33:37           lo   1370.00   1370.00    136.59    136.59      0.00      0.00      0.00      0.00
^C


Average:        IFACE   rxpck/s   txpck/s    rxkB/s    txkB/s   rxcmp/s   txcmp/s  rxmcst/s   %ifutil
Average:         eth0      0.00      0.00      0.00      0.00      0.00      0.00      0.00      0.00
Average:           lo   1309.97   1309.97    130.58    130.58      0.00      0.00      0.00      0.00
root@22edad7d6847:/troubleshoot#

```