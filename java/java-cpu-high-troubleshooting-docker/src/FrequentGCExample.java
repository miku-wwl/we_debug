import java.util.ArrayList;
import java.util.List;

public class FrequentGCExample {
    public static void main(String[] args) {
        System.out.println("频繁GC示例已启动...");
        System.out.println("使用top, jstack, jstat等命令进行故障排查");
        
        // 配置JVM参数以限制堆大小，加速GC
        // -Xmx64m -Xms64m -XX:+PrintGCDetails
        
        // 创建一个线程不断分配内存
        Thread allocationThread = new Thread(() -> {
            List<byte[]> list = new ArrayList<>();
            while (true) {
                // 每次分配1MB内存
                list.add(new byte[1024 * 1024]);
                
                // 随机移除一些对象，模拟对象生命周期
                if (list.size() > 10 && Math.random() > 0.7) {
                    int index = (int) (Math.random() * list.size());
                    list.remove(index);
                }
                
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        allocationThread.setName("Allocation-Thread");
        allocationThread.start();
        
        // 主线程保持运行
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}    