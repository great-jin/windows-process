<h1 align="center">Windows Process</h1>

**其他语言版本: [English](/README.md), [中文](/doc/README_ZH.md).**

`Windows Process` 是一款 `Windows` 下的端口进程管理工具，能够直观的展示当前运行进程对应的端口占用以及其进程号，并且提供了进程关闭能力。

## 原理
程序的实现原理是基于 `netstat -ano | findstr <port>` 和 `taskkill -PID <pid> -F` 两行命令。

在我的日常开发中，我常需要查看某个端口被哪个进程所占用，所以就需要用到上述的两个命令。

虽然整个过程并非复杂到难以接受，但在高频使用下需要重复先找到这两行命令再打开 `cmd` 命令输入，总体上还是有点繁琐。

因此，我基于 `Java Swing` 和 `Process` 开发了此程序，提供了可用性更高的 `GUI` 程序实现上述操作。


## 手册
程序提供了两种运行方式，更推荐方式二。

### 1. jar 运行
在仓库的 `release` 页面下载 `windows process.zip` 文件，其中包含了 `start.vbs` 与 `windows process.jar` 两个文件，双击前者即可启动程序。

注意: 此方式需要 `Windows` 配置了 JDK 环境，此程序基于 `JDK 8` 开发，在 `JDK 11` 与 `JDK 17`环境下也测试能够正常运行。

### 2. exe 运行
同方式一，在 `release` 页面下载 `windows process.exe` 程序，完成后双击即可启动。

在此方式下启动程序后会先弹出一条 `exec4j9` 提示，因此是通过该工具实现程序打包，点击确认即可。


## Issue
在使用程序的过程中如果发现问题或有任何疑问，欢迎在仓库的 `Issue` 页面提出。
