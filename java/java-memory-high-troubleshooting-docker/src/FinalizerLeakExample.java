import java.util.ArrayList;
import java.util.List;

public class FinalizerLeakExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Finalizer内存泄漏示例已启动...");
        System.out.println("使用top, jstat, jmap, jcmd等命令进行故障排查");
        
        List<BigObject> list = new ArrayList<>();
        
        while (true) {
            // 创建大量带有finalize方法的对象
            list.add(new BigObject());
            
            // 每添加100个对象，移除一部分模拟GC
            if (list.size() % 100 == 0) {
                for (int i = 0; i < 50; i++) {
                    list.remove(0);
                }
            }
            
            System.out.println("已创建对象: " + list.size());
            Thread.sleep(50);
        }
    }
    
    static class BigObject {
        private final byte[] data = new byte[1024 * 1024]; // 1MB
        
        @Override
        protected void finalize() throws Throwable {
            // 模拟长时间运行的finalize方法
            Thread.sleep(100);
            super.finalize();
        }
    }
}    