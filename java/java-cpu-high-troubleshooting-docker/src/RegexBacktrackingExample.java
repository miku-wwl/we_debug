import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexBacktrackingExample {
    public static void main(String[] args) {
        System.out.println("正则表达式回溯示例已启动...");
        System.out.println("使用top, jstack, jstat等命令进行故障排查");
        
        // 复杂的正则表达式，容易导致回溯
        String regex = "^(a+)+$";
        Pattern pattern = Pattern.compile(regex);
        
        // 创建一个线程执行正则匹配
        Thread regexThread = new Thread(() -> {
            while (true) {
                // 构造一个会导致大量回溯的字符串
                String input = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa!";
                Matcher matcher = pattern.matcher(input);
                try {
                    // 执行匹配，这将导致大量回溯和CPU消耗
                    boolean result = matcher.matches();
                    System.out.println("匹配结果: " + result);
                } catch (StackOverflowError e) {
                    System.out.println("发生StackOverflowError，继续尝试...");
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        regexThread.setName("Regex-Backtracking-Thread");
        regexThread.start();
        
        // 主线程保持运行
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}    