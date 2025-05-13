public class CPUIntensiveExample {
    public static void main(String[] args) {
        // 创建多个CPU密集型线程
        for (int i = 0; i < 4; i++) {
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