import java.io.*;
import java.nio.channels.FileChannel;

public class FileCopyExample {
    private static final String SOURCE_FILE = "/troubleshoot/data/source.dat";
    private static final String TARGET_FILE = "/troubleshoot/data/target.dat";
    private static final int FILE_SIZE = 1024 * 1024 * 500; // 500MB

    public static void main(String[] args) throws InterruptedException {
        System.out.println("大文件复制示例已启动...");
        System.out.println("使用iotop, iostat, strace等命令进行故障排查");
        
        // 初始化源文件
        initializeFile();
        
        while (true) {
            try {
                System.out.println("开始复制文件...");
                copyFile(SOURCE_FILE, TARGET_FILE);
                System.out.println("文件复制完成，准备下一次复制...");
                Thread.sleep(1000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void initializeFile() {
        try (FileOutputStream fos = new FileOutputStream(SOURCE_FILE)) {
            // 创建指定大小的文件
            byte[] buffer = new byte[1024 * 1024]; // 1MB
            for (int i = 0; i < FILE_SIZE / buffer.length; i++) {
                fos.write(buffer);
            }
            System.out.println("已初始化源文件: " + SOURCE_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void copyFile(String sourcePath, String targetPath) throws IOException {
        try (FileChannel sourceChannel = new FileInputStream(sourcePath).getChannel();
             FileChannel targetChannel = new FileOutputStream(targetPath).getChannel()) {
            
            long position = 0;
            long count = sourceChannel.size();
            
            // 使用transferTo进行高效复制
            while (position < count) {
                position += sourceChannel.transferTo(position, count - position, targetChannel);
            }
        }
    }
}    