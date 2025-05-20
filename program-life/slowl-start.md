Java项目启动异常缓慢定位日志：

### **启动异常定位方法说明**  
#### **通过线程dump分析主线程阻塞点**  
**操作步骤**  
   - 在IDEA中启动项目调试模式，待启动卡顿期间，点击菜单栏 **Run > Get Thread Dump** 生成线程快照；  
   - 打开线程dump文件，搜索关键词 **"main"** 定位主线程堆栈信息；  
   - 重点查看主线程当前执行栈帧，若出现类似以下堆栈：  
     ```java  
     "main" #1 prio=5 os_prio=0 tid=0x0000 main@0x0000  
        at com.alibaba.druid.pool.DruidDataSource.init(DruidDataSource.java:618)  
        at com.alibaba.druid.pool.DruidDataSource.getConnection(DruidDataSource.java:1199)  
        ...（项目初始化代码）  
     ```  
     表明程序在 **Druid数据源初始化阶段** 阻塞，且可能伴随 **数据库连接异常**（如超时、认证失败等）。