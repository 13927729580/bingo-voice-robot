# bingo-voice-robot
bingo语音机器人 灵感来自 [叮当机器人](https://github.com/wzpan/dingdang-robot/)

## 硬件要求
  - Raspberry Pi 全系列，或其他 Linux 主机；
  - USB 麦克风（建议选购麦克风阵列）；
  - 音箱（不建议蓝牙音箱）；

## 软件要求
  - java 8 安装命令  sudo apt-get install openjdk-8-dbg
  - maven 随便哪个版本吧，主要是用于打包用的。如果在其它机器上打好包。就不需要

## 安装
  - 打包命令：  mvn clean -DskipTests package -am -pl robot-runner

## 配置
  - 参见：[配置wiki](http://git.oschina.net/zlbroot/bingo-voice-robot/wikis/%E9%85%8D%E7%BD%AE)
  - 参见 robot-runner\src\main\resources\profile.yml
  - 主要是写百度语音appKey信息， 图灵机器人。

## 运行
  - 找到 robot-runner/target/robot-runner-jar-with-dependencies.jar 上传到你自己的树莓派上。
  - 将profile.yml复制到树莓派上robot-runner-jar-with-dependencies.jar 同一个目录下。
  - java -jar robot-runner-jar-with-dependencies.jar 直接运行。

## 免责声明
  - 本项目只用作个人学习研究，如因使用本项目导致任何损失，本人概不负责。
