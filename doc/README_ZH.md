<h1 align="center">Windows Process</h1>

**其他语言版本: [English](/README.md), [中文](/doc/README_ZH.md).**

`Windows Process` 是一款 `Windows` 下的端口进程管理工具，能够直观的展示当前运行进程对应的端口占用以及其进程号，并且提供了进程关闭能力。

## 原理
程序的实现原理是基于 `netstat -ano | findstr <port>` 和 `taskkill -PID <pid> -F` 两行命令。

在我的日常开发中，我常需要查看某个端口被哪个进程所占用，所以就需要用到上述的两个命令。

虽然整个过程并非复杂到难以接受，但在高频使用下需要重复先找到这两行命令再打开 `cmd` 命令输入，总体上还是有点繁琐。

因此，我基于 `Java Swing` 和 `Process` 开发了此程序，提供了可用性更高的 `GUI` 程序实现上述操作。


## 手册
程序提供两种方式运行，区别在于其中一种需要系统环境配置了 `JRE`。

### 方式一
在 `release` 下载 `windcess.exe` 文件即可运行，此方式要求系统环境变量配置了 `jdk`，且版本不低于 `17`。

### 方式二
在 `release` 下载 `windcess-<version>-portable.zip` 文件，解压后运行 `windcess.exe` 文件即可。


## Issue
在使用程序的过程中如果发现问题或有任何疑问，欢迎在仓库的 `Issue` 页面提出。
