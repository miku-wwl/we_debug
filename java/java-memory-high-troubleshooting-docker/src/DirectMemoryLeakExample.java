import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DirectMemoryLeakExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("直接内存泄漏示例已启动...");
        System.out.println("使用top, jstat, jmap, jcmd等命令进行故障排查");
        
        List<ByteBuffer> buffers = new ArrayList<>();
        
        while (true) {
            // 分配1MB的直接内存
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);
            buffers.add(buffer);
            
            System.out.println("已分配直接内存: " + buffers.size() + "MB");
            Thread.sleep(100);
            
            // 模拟内存泄漏：不释放直接内存
            // 正确做法应该调用 buffer.clear() 或 buffer = null; System.gc();
        }
    }
}    