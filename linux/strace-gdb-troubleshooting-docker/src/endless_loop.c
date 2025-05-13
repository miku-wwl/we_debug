#include <stdio.h>
#include <unistd.h>

int condition = 1;

void check_condition() {
    // 模拟一个永远不会改变条件的函数
    sleep(1);
}

int main() {
    printf("程序开始运行...\n");
    
    while (condition) {
        // 模拟一个死循环
        check_condition();
    }
    
    printf("程序正常结束。\n");
    return 0;
}    