import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// 模拟一个缓存系统中的内存泄漏
public class MemoryLeakExample {
    // 静态Map用于缓存对象，但未正确实现清除机制
    private static final Map<String, LargeObject> cache = new HashMap<>();
    private static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("内存泄漏示例已启动...");
        System.out.println("使用jmap和jstack命令进行故障排查");
        
        // 模拟一个Web服务器，持续接收请求
        while (true) {
            handleRequest();
            Thread.sleep(100);
            
            // 每10秒打印一次堆使用情况
            if (random.nextInt(100) < 10) {
                printHeapUsage();
            }
        }
    }

    // 处理请求并模拟缓存泄漏
    private static void handleRequest() {
        // 生成随机ID作为缓存键
        String key = "user-" + random.nextInt(10000);
        
        // 模拟从数据库加载数据
        LargeObject data = loadDataFromDatabase(key);
        
        // 模拟缓存系统 - 但这里存在内存泄漏：对象被无限期保留
        cache.put(key, data);
        
        // 模拟只有10%的缓存清除率，导致对象逐渐积累
        if (random.nextInt(100) < 10) {
            removeRandomEntry();
        }
    }

    // 从数据库加载数据（模拟）
    private static LargeObject loadDataFromDatabase(String key) {
        return new LargeObject(key);
    }

    // 随机移除一个缓存项（但移除率太低导致泄漏）
    private static void removeRandomEntry() {
        if (!cache.isEmpty()) {
            String randomKey = cache.keySet().iterator().next();
            cache.remove(randomKey);
        }
    }

    // 打印堆使用情况
    private static void printHeapUsage() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long maxMemory = runtime.maxMemory();
        
        System.out.printf("堆内存使用: %d MB / %d MB (最大: %d MB)%n",
            (totalMemory - freeMemory) / (1024 * 1024),
            totalMemory / (1024 * 1024),
            maxMemory / (1024 * 1024));
    }

    // 模拟一个占用大量内存的对象
    static class LargeObject {
        private final String id;
        private final byte[] data;

        public LargeObject(String id) {
            this.id = id;
            // 每个对象占用1MB内存
            this.data = new byte[1024 * 1024];
        }

        public String getId() {
            return id;
        }

        @Override
        protected void finalize() throws Throwable {
            // 用于验证对象是否被垃圾回收
            System.out.println("对象被垃圾回收: " + id);
            super.finalize();
        }
    }
}    