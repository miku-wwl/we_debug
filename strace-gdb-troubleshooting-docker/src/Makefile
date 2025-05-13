CC = gcc
CFLAGS = -g -O0

all: segfault endless_loop resource_leak blocked_syscall

segfault: segfault.c
    $(CC) $(CFLAGS) -o segfault segfault.c

endless_loop: endless_loop.c
    $(CC) $(CFLAGS) -o endless_loop endless_loop.c

resource_leak: resource_leak.c
    $(CC) $(CFLAGS) -o resource_leak resource_leak.c

blocked_syscall: blocked_syscall.c
    $(CC) $(CFLAGS) -o blocked_syscall blocked_syscall.c

clean:
    rm -f segfault endless_loop resource_leak blocked_syscall    