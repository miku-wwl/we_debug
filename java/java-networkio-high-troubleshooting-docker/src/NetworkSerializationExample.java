import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkSerializationExample {
    private static final int PORT = 9090;
    private static final int THREAD_POOL_SIZE = 20;

    public static void main(String[] args) {
        System.out.println("网络序列化性能问题示例已启动...");
        System.out.println("使用netstat, ss, tcpdump等命令进行故障排查");
        
        // 启动服务器
        startServer();
        
        // 启动客户端
        startClients();
    }
    
    private static void startServer() {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("服务器已启动，监听端口: " + PORT);
                
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    executor.submit(() -> handleClient(clientSocket));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    private static void handleClient(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
            
            // 读取大型对象
            @SuppressWarnings("unchecked")
            List<DataObject> dataList = (List<DataObject>) ois.readObject();
            
            System.out.println("接收到对象列表，大小: " + dataList.size());
            
            // 修改数据并返回
            dataList.forEach(obj -> obj.setValue(obj.getValue() * 2));
            
            oos.writeObject(dataList);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void startClients() {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            executor.submit(() -> {
                while (true) {
                    try (Socket socket = new Socket("localhost", PORT);
                         ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                         ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
                        
                        // 创建大型对象列表
                        List<DataObject> dataList = new ArrayList<>();
                        for (int j = 0; j < 10000; j++) {
                            dataList.add(new DataObject(j, "Data-" + j));
                        }
                        
                        // 发送对象
                        oos.writeObject(dataList);
                        oos.flush();
                        
                        // 接收响应
                        @SuppressWarnings("unchecked")
                        List<DataObject> response = (List<DataObject>) ois.readObject();
                        
                        System.out.println("接收到响应，大小: " + response.size());
                        
                        Thread.sleep(500);
                    } catch (IOException | ClassNotFoundException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    
    static class DataObject implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private int id;
        private String name;
        private double value;
        private byte[] data;
        
        public DataObject(int id, String name) {
            this.id = id;
            this.name = name;
            this.value = Math.random() * 1000;
            this.data = new byte[1024]; // 1KB
        }
        
        public int getId() { return id; }
        public String getName() { return name; }
        public double getValue() { return value; }
        public void setValue(double value) { this.value = value; }
    }
}    