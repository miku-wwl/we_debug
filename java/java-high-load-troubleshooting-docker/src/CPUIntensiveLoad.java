public class CPUIntensiveLoad {
    public static void main(String[] args) {
        System.out.println("CPU密集型负载示例已启动...");
        System.out.println("使用top, htop, jstack等命令进行故障排查");
        
        // 创建多个线程进行CPU密集型计算
        int threadCount = Runtime.getRuntime().availableProcessors();
        System.out.println("启动 " + threadCount + " 个CPU密集型线程");
        
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                while (true) {
                    // 执行无限循环的计算任务
                    double result = 0;
                    for (int j = 0; j < 1000000; j++) {
                        result += Math.sqrt(j) * Math.sin(j) * Math.cos(j);
                    }
                }
            }, "CPU-Thread-" + i).start();
        }
        
        // 保持主线程运行
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}    