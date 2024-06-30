<h1 align="center">Windows Process</h1>

**Read this in other languages: [English](/README.md), [中文](/doc/README_ZH.md).**

The `Windows Process` is a application for windows port process management. You can easily use it to show the currently running process, it directly give you the process running port and pid, also provide the ability to kill the process.

## Theory
The application it was simple enough base on the command of `netstat -ano | findstr <port>` and `taskkill -PID <pid> -F`.

In my develop lift, I frequently needed to find what process was currently occupancy some specify port. Basically, I was use the command that just mentioned, imaging the whole progress: open the `cmd` and type the two command one by one.

In fact, it is not difficult enough that people will intolerances, but it is kind tedious if you use it a lot.

So, here we are, I base on `Java Swing` and `Process` develop this program, provide the easy use `GUI` for above operation.


## Manual
There is two way to running the program.

### 1. Run with jar
See the repository release and download the `windows process.zip`, it contain two file `start.vbs` and `windows process.jar`, just double-click the `start.vbs` then application will start.

Notice: The required the `JDK` environment in you PC, the program was developed under `JDK 8`，but it also functional in `JDK 11` or `JDK 17`.

### 2. Run with exe
In this way, it was much easier, just download `windows process.exe` and double click.

It will pop a notice by `exec4j9`, because I use it for package program, just click the confirm and move on.


## Issue
In the use procedure if you have found the bug or have any question, you can ask in this repository issue page.
