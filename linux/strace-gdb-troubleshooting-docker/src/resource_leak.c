#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main() {
    printf("程序开始运行...\n");
    
    while (1) {
        // 持续分配内存但不释放
        char *ptr = malloc(1024 * 1024); // 每次分配1MB
        if (!ptr) {
            perror("内存分配失败");
            return 1;
        }
        sleep(1);
    }
    
    return 0;
}    