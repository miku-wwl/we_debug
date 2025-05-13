import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HeapMemoryLeakExample {
    // 模拟缓存泄漏
    private static final Map<String, List<byte[]>> cache = new HashMap<>();
    private static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("堆内存泄漏示例已启动...");
        System.out.println("使用top, jstat, jmap, jcmd等命令进行故障排查");
        
        // 持续向缓存中添加数据，模拟内存泄漏
        while (true) {
            String key = "data-" + random.nextInt(1000);
            List<byte[]> dataList = new ArrayList<>();
            
            // 每个列表包含10个1MB的数组
            for (int i = 0; i < 10; i++) {
                dataList.add(new byte[1024 * 1024]); // 1MB
            }
            
            // 添加到缓存，但很少移除
            cache.put(key, dataList);
            
            // 模拟只有10%的清除率
            if (random.nextInt(100) < 10 && !cache.isEmpty()) {
                String randomKey = cache.keySet().iterator().next();
                cache.remove(randomKey);
            }
            
            System.out.println("缓存大小: " + cache.size() + " 条目");
            Thread.sleep(100);
        }
    }
}    