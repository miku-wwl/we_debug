import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

// 动态生成大量类导致Metaspace溢出
public class MetaspaceOOMExample {
    public static void main(String[] args) {
        System.out.println("元空间溢出示例已启动...");
        System.out.println("使用top, jstat, jmap, jcmd等命令进行故障排查");
        
        List<Object> proxies = new ArrayList<>();
        Class<?>[] interfaces = new Class<?>[]{Runnable.class};
        
        try {
            while (true) {
                // 动态生成代理类
                Object proxy = Proxy.newProxyInstance(
                    MetaspaceOOMExample.class.getClassLoader(),
                    interfaces,
                    (proxy1, method, args1) -> {
                        // 空实现
                        return null;
                    }
                );
                proxies.add(proxy);
                
                System.out.println("已生成代理类: " + proxies.size());
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (OutOfMemoryError e) {
            System.out.println("发生OutOfMemoryError: " + e.getMessage());
            throw e;
        }
    }
}    