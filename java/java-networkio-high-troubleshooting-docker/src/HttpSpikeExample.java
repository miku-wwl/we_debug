import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpSpikeExample {
    private static final String TARGET_URL = "https://example.com";
    private static final int THREAD_COUNT = 20;

    public static void main(String[] args) {
        System.out.println("HTTP请求高峰示例已启动...");
        System.out.println("使用netstat, ss, tcpdump等命令进行故障排查");
        
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        HttpClient client = HttpClient.newBuilder().build();
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                while (true) {
                    try {
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(TARGET_URL))
                                .build();
                        
                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        System.out.println("HTTP响应码: " + response.statusCode());
                        
                        // 短暂休眠，避免过于频繁
                        Thread.sleep(100);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}    