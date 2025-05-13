import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class FileWriteSpikeExample {
    private static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("文件写入高峰示例已启动...");
        System.out.println("使用iotop, iostat, strace等命令进行故障排查");
        
        String filePath = "/troubleshoot/data/write_spike_test.dat";
        
        while (true) {
            try (FileOutputStream fos = new FileOutputStream(filePath, true)) {
                // 每次写入1MB随机数据
                byte[] data = new byte[1024 * 1024];
                random.nextBytes(data);
                fos.write(data);
                fos.flush();
                
                System.out.println("已写入1MB数据");
                Thread.sleep(100);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}    