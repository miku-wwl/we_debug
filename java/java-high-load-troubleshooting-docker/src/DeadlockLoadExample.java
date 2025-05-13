import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockLoadExample {
    private static final Lock lock1 = new ReentrantLock();
    private static final Lock lock2 = new ReentrantLock();

    public static void main(String[] args) {
        System.out.println("死锁导致系统负载高示例已启动...");
        System.out.println("使用top, jstack等命令进行故障排查");
        
        // 创建并启动线程导致死锁
        Thread thread1 = new Thread(() -> {
            while (true) {
                lock1.lock();
                try {
                    System.out.println("线程1获取了锁1");
                    Thread.sleep(100);
                    
                    System.out.println("线程1尝试获取锁2");
                    lock2.lock();
                    try {
                        System.out.println("线程1获取了锁2");
                    } finally {
                        lock2.unlock();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock1.unlock();
                }
            }
        }, "Deadlock-Thread-1");
        
        Thread thread2 = new Thread(() -> {
            while (true) {
                lock2.lock();
                try {
                    System.out.println("线程2获取了锁2");
                    Thread.sleep(100);
                    
                    System.out.println("线程2尝试获取锁1");
                    lock1.lock();
                    try {
                        System.out.println("线程2获取了锁1");
                    } finally {
                        lock1.unlock();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock2.unlock();
                }
            }
        }, "Deadlock-Thread-2");
        
        thread1.start();
        thread2.start();
        
        // 保持主线程运行
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}    