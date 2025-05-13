import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockWithHighCPUExample {
    private static final Lock lock1 = new ReentrantLock();
    private static final Lock lock2 = new ReentrantLock();

    public static void main(String[] args) {
        System.out.println("死锁导致CPU高使用率示例已启动...");
        System.out.println("使用top, jstack, jstat等命令进行故障排查");
        
        // 创建两个线程，形成死锁
        Thread thread1 = new Thread(() -> {
            while (true) {
                lock1.lock();
                try {
                    // 模拟一些计算
                    for (int i = 0; i < 1000000; i++) {
                        Math.sqrt(i);
                    }
                    
                    System.out.println("Thread 1 acquired lock1, trying to acquire lock2...");
                    lock2.lock();
                    try {
                        System.out.println("Thread 1 acquired both locks");
                    } finally {
                        lock2.unlock();
                    }
                } finally {
                    lock1.unlock();
                }
            }
        });
        thread1.setName("Deadlock-Thread-1");
        thread1.start();
        
        Thread thread2 = new Thread(() -> {
            while (true) {
                lock2.lock();
                try {
                    // 模拟一些计算
                    for (int i = 0; i < 1000000; i++) {
                        Math.sqrt(i);
                    }
                    
                    System.out.println("Thread 2 acquired lock2, trying to acquire lock1...");
                    lock1.lock();
                    try {
                        System.out.println("Thread 2 acquired both locks");
                    } finally {
                        lock1.unlock();
                    }
                } finally {
                    lock2.unlock();
                }
            }
        });
        thread2.setName("Deadlock-Thread-2");
        thread2.start();
        
        // 主线程保持运行
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}    