import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BlockedThreadExample {
    public static void main(String[] args) throws IOException {
        // 创建一个ServerSocket但不接受连接
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server started on port 8080");

        // 创建一个线程，阻塞在accept()调用上
        Thread acceptThread = new Thread(() -> {
            try {
                System.out.println("Waiting for a connection...");
                Socket clientSocket = serverSocket.accept(); // 这行将一直阻塞
                System.out.println("Connection accepted: " + clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        acceptThread.setName("Accept-Thread");
        acceptThread.start();

        // 主线程保持运行
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}    