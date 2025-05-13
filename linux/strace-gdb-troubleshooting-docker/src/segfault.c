#include <stdio.h>

void func2() {
    int *ptr = NULL;
    *ptr = 10; // 这里会引发段错误
}

void func1() {
    func2();
}

int main() {
    printf("程序开始运行...\n");
    func1();
    printf("程序正常结束。\n");
    return 0;
}    