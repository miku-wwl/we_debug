import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class RandomFileAccessExample {
    private static final Random random = new Random();
    private static final int FILE_SIZE = 1024 * 1024 * 100; // 100MB
    private static final String FILE_PATH = "/troubleshoot/data/random_access_test.dat";

    public static void main(String[] args) throws InterruptedException {
        System.out.println("随机文件访问示例已启动...");
        System.out.println("使用iotop, iostat, strace等命令进行故障排查");
        
        // 初始化测试文件
        initializeFile();
        
        while (true) {
            try (RandomAccessFile raf = new RandomAccessFile(FILE_PATH, "rw")) {
                // 随机位置读写
                for (int i = 0; i < 1000; i++) {
                    long position = random.nextLong(FILE_SIZE);
                    raf.seek(position);
                    
                    // 读取一个字节
                    raf.readByte();
                    
                    // 写入一个随机字节
                    raf.writeByte(random.nextInt(256));
                }
                
                System.out.println("完成1000次随机读写");
                Thread.sleep(50);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void initializeFile() {
        try (RandomAccessFile raf = new RandomAccessFile(FILE_PATH, "rw")) {
            // 创建指定大小的文件
            raf.setLength(FILE_SIZE);
            System.out.println("已初始化测试文件: " + FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}    