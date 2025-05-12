<h1 align="center">Windows Process</h1>

**Read this in other languages: [English](/README.md), [中文](/doc/README_ZH.md).**

The `Windows Process` is a application for windows port process management. You can easily use it to show the currently running process, it directly give you the process running port and pid, also provide the ability to kill the process.



## Mechanism
The application it was simple enough base on the command of `netstat -ano | findstr <port>` and `taskkill -PID <pid> -F`.

In my develop lift, I frequently needed to find what process was currently occupancy some specify port. Basically, I was use the command that just mentioned, imaging the whole progress: open the `cmd` and type the two command one by one.

In fact, it is not difficult enough that people will intolerances, but it is kind tedious if you use it a lot.

So, here we are, I base on `Java Swing` and `Process` develop this program, provide the easy use `GUI` for above operation.



## Manual
There is two kind files in release, the difference is one of them require `jre` in your PC. 

### Choice 1
Download `windcess.exe` file from release page, the file is smaller but requires `jdk` environmental variable.

**Notice**: In this case, it required `jdk >= 17`.

### Choice 2
Download the `windcess-<version>-portable.zip`, then unzip it and you free to go.



## Issue
In the use procedure if you have found the bug or have any question, you can ask in this repository issue page.
