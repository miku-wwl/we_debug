import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioFileLockingExample {
    private static final String FILE_PATH = "/troubleshoot/data/file_locking_test.dat";

    public static void main(String[] args) {
        System.out.println("文件锁争用示例已启动...");
        System.out.println("使用iotop, iostat, strace等命令进行故障排查");
        
        // 创建多个线程进行文件锁争用
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        for (int i = 0; i < 10; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    while (true) {
                        try (RandomAccessFile file = new RandomAccessFile(FILE_PATH, "rw")) {
                            // 获取独占锁
                            FileLock lock = file.getChannel().lock();
                            try {
                                // 模拟文件操作
                                for (int j = 0; j < 1000; j++) {
                                    file.writeInt(j);
                                }
                                
                                // 随机睡眠
                                Thread.sleep(10);
                            } finally {
                                // 释放锁
                                lock.release();
                            }
                        }
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        
        // 主线程保持运行
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}    