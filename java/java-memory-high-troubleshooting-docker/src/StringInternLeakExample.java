import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StringInternLeakExample {
    private static final List<String> internedStrings = new ArrayList<>();
    private static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("String.intern()内存泄漏示例已启动...");
        System.out.println("使用top, jstat, jmap, jcmd等命令进行故障排查");
        
        while (true) {
            // 生成随机字符串并intern
            String str = generateRandomString(1000);
            String interned = str.intern();
            internedStrings.add(interned);
            
            System.out.println("已intern字符串: " + internedStrings.size());
            Thread.sleep(100);
        }
    }
    
    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + random.nextInt(26)));
        }
        return sb.toString();
    }
}    