import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ContextSwitchLoad {
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        System.out.println("上下文切换频繁导致系统负载高示例已启动...");
        System.out.println("使用vmstat, pidstat等命令进行故障排查");
        
        // 创建大量线程，导致频繁上下文切换
        int threadCount = 100;
        System.out.println("创建 " + threadCount + " 个线程");
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                while (true) {
                    // 增加计数器，模拟线程活动
                    counter.incrementAndGet();
                    
                    // 短暂休眠，保持线程活跃但不过载CPU
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        
        // 主线程定期打印计数器
        new Thread(() -> {
            while (true) {
                System.out.println("计数器值: " + counter.get());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
        // 保持主线程运行
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}    