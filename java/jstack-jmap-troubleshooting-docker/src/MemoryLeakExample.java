import java.util.ArrayList;
import java.util.List;

public class MemoryLeakExample {
    private static final List<Object> leakList = new ArrayList<>();

    public static void main(String[] args) {
        // 模拟内存泄漏
        while (true) {
            leakList.add(new byte[1024 * 1024]); // 每次添加1MB对象
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}    