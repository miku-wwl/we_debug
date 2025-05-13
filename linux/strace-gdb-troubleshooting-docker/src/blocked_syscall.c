#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>

int main() {
    printf("程序开始运行...\n");
    
    // 打开一个不存在的文件进行读取，会导致阻塞
    int fd = open("/nonexistent_file", O_RDONLY);
    if (fd == -1) {
        perror("打开文件失败");
        // 继续执行，模拟部分功能正常的情况
    } else {
        char buffer[1024];
        // 尝试读取文件，会永久阻塞
        printf("尝试读取文件...\n");
        ssize_t bytes_read = read(fd, buffer, sizeof(buffer));
        if (bytes_read == -1) {
            perror("读取文件失败");
        } else {
            printf("读取了 %zd 字节\n", bytes_read);
        }
        close(fd);
    }
    
    // 程序继续执行其他任务
    printf("程序继续运行其他任务...\n");
    while (1) {
        sleep(1);
    }
    
    return 0;
}    