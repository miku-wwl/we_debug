import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServerExample {
    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 100;

    public static void main(String[] args) {
        System.out.println("TCP服务器高负载示例已启动...");
        System.out.println("使用netstat, ss, tcpdump等命令进行故障排查");
        
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("服务器已启动，监听端口: " + PORT);
            
            while (true) {
                // 接受客户端连接
                Socket clientSocket = serverSocket.accept();
                System.out.println("接受新连接: " + clientSocket.getInetAddress());
                
                // 处理客户端请求
                executor.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void handleClient(Socket clientSocket) {
        try (InputStream in = clientSocket.getInputStream();
             OutputStream out = clientSocket.getOutputStream()) {
            
            byte[] buffer = new byte[4096];
            int bytesRead;
            
            // 读取客户端数据
            while ((bytesRead = in.read(buffer)) != -1) {
                // 回显数据给客户端
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            // 忽略异常
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}    