import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DnsLookupSpikeExample {
    private static final String[] DOMAINS = {
        "example.com", "google.com", "facebook.com", "twitter.com",
        "amazon.com", "apple.com", "microsoft.com", "github.com"
    };
    private static final int THREAD_COUNT = 50;

    public static void main(String[] args) {
        System.out.println("DNS查询高峰示例已启动...");
        System.out.println("使用netstat, ss, tcpdump等命令进行故障排查");
        
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                while (true) {
                    try {
                        // 随机选择一个域名进行解析
                        String domain = DOMAINS[(int) (Math.random() * DOMAINS.length)];
                        
                        // 执行DNS查询
                        InetAddress[] addresses = InetAddress.getAllByName(domain);
                        
                        System.out.println("解析域名: " + domain + ", 结果数量: " + addresses.length);
                        
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}    