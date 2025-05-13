import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class IOIntensiveLoad {
    private static final Random random = new Random();
    private static final String FILE_PATH = "/tmp/io_load_test.dat";
    private static final int BUFFER_SIZE = 1024 * 1024; // 1MB

    public static void main(String[] args) {
        System.out.println("IO密集型负载示例已启动...");
        System.out.println("使用iotop, iostat, strace等命令进行故障排查");
        
        // 创建并启动多个IO密集型线程
        int threadCount = Runtime.getRuntime().availableProcessors();
        System.out.println("启动 " + threadCount + " 个IO密集型线程");
        
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        // 生成随机数据
                        byte[] data = new byte[BUFFER_SIZE];
                        random.nextBytes(data);
                        
                        // 写入文件
                        try (FileOutputStream fos = new FileOutputStream(FILE_PATH, true)) {
                            fos.write(data);
                            fos.flush();
                        }
                        
                        // 短暂休眠
                        Thread.sleep(10);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "IO-Thread-" + i).start();
        }
        
        // 保持主线程运行
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}    