import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GCStressExample {
    private static final Random random = new Random();
    private static final List<byte[]> list = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("GC压力测试示例已启动...");
        System.out.println("使用jstat, jinfo, jcmd命令进行故障排查");
        
        long interval = 100;
        if (args.length > 0) {
            try {
                interval = Long.parseLong(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("无效的间隔参数: " + args[0]);
                System.exit(1);
            }
        }
        
        // 持续分配内存
        while (true) {
            // 随机分配不同大小的对象
            int size = 1024 * (random.nextInt(100) + 1); // 1KB到100KB
            list.add(new byte[size]);
            
            // 随机移除一些对象
            if (list.size() > 1000 && random.nextBoolean()) {
                int index = random.nextInt(list.size());
                list.remove(index);
            }
            
            // 触发一些CPU活动
            for (int i = 0; i < 10000; i++) {
                Math.sin(random.nextDouble());
            }
            
            Thread.sleep(interval);
        }
    }
}    