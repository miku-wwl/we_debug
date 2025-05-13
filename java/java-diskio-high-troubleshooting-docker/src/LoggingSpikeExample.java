import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class LoggingSpikeExample {
    private static final Random random = new Random();
    private static final String LOG_PATH = "/troubleshoot/data/logging_spike.log";

    public static void main(String[] args) throws InterruptedException {
        System.out.println("日志写入高峰示例已启动...");
        System.out.println("使用iotop, iostat, strace等命令进行故障排查");
        
        try (FileWriter writer = new FileWriter(LOG_PATH, true)) {
            while (true) {
                // 生成1KB的日志行
                StringBuilder logLine = new StringBuilder();
                for (int i = 0; i < 1024; i++) {
                    logLine.append((char) ('a' + random.nextInt(26)));
                }
                logLine.append("\n");
                
                // 写入日志
                writer.write(logLine.toString());
                writer.flush();
                
                // 每秒写入100次，约100KB/s
                Thread.sleep(10);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}    