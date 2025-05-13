import java.util.ArrayList;
import java.util.List;

public class HeapOOMExample {
    public static void main(String[] args) {
        System.out.println("堆内存溢出示例已启动...");
        System.out.println("使用jstat, jinfo, jcmd命令进行故障排查");
        
        List<byte[]> list = new ArrayList<>();
        int size = 1024 * 1024; // 1MB
        
        try {
            while (true) {
                list.add(new byte[size]);
                System.out.println("已分配: " + list.size() + "MB");
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (OutOfMemoryError e) {
            System.out.println("发生OutOfMemoryError: " + e.getMessage());
            throw e;
        }
    }
}    