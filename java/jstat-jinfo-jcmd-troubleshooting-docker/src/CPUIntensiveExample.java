public class CPUIntensiveExample {
    public static void main(String[] args) {
        System.out.println("CPU密集型示例已启动...");
        System.out.println("使用jstat, jinfo, jcmd命令进行故障排查");
        
        // 创建多个CPU密集型线程
        int threadCount = Runtime.getRuntime().availableProcessors();
        System.out.println("创建" + threadCount + "个CPU密集型线程");
        
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    // 执行密集计算
                    Math.pow(999, 999);
                }
            });
            thread.setName("CPU-Intensive-Thread-" + i);
            thread.start();
        }
        
        // 主线程保持运行
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}    