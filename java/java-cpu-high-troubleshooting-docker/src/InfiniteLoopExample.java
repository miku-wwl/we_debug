public class InfiniteLoopExample {
    public static void main(String[] args) {
        System.out.println("死循环示例已启动...");
        System.out.println("使用top, jstack, jstat等命令进行故障排查");
        
        // 创建一个死循环线程
        Thread loopThread = new Thread(() -> {
            while (true) {
                // 空循环，消耗CPU资源
            }
        });
        loopThread.setName("Infinite-Loop-Thread");
        loopThread.start();
        
        // 主线程保持运行
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}    