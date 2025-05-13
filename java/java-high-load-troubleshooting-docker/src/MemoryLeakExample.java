import java.util.ArrayList;
import java.util.List;

public class MemoryLeakExample {
    private static final List<byte[]> leakContainer = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("内存泄漏导致系统负载高示例已启动...");
        System.out.println("使用top, jstat, jmap等命令进行故障排查");
        
        // 模拟HTTP服务器
        startHttpServer();
    }
    
    private static void startHttpServer() {
        com.sun.net.httpserver.HttpServer server;
        try {
            server = com.sun.net.httpserver.HttpServer.create(new java.net.InetSocketAddress(8080), 0);
            
            // 注册会导致内存泄漏的接口
            server.createContext("/api/leak", exchange -> {
                try {
                    // 每次请求分配1MB内存并不释放
                    leakContainer.add(new byte[1024 * 1024]);
                    
                    String response = "已分配内存，当前泄漏容器大小: " + leakContainer.size() + "MB";
                    byte[] responseBytes = response.getBytes();
                    exchange.sendResponseHeaders(200, responseBytes.length);
                    exchange.getResponseBody().write(responseBytes);
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(500, 0);
                } finally {
                    exchange.close();
                }
            });
            
            server.start();
            System.out.println("HTTP服务器已启动，监听端口: 8080");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}    