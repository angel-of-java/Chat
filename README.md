# Chat
### 项目简介
这是我在学习完Java Socket之后写的一个匿名聊天室，客户端连接到服务器后可以向聊天室发送消息。本项目使用的都是最简单的基础知识。
### 工作流程
启动服务器 -> 启动客户端 -> 客户端连接到服务器 -> 客户端发送消息 -> 服务器接收并转发到每一个客户端 -> 客户端接收服务器转发的消息
### 注意
* 启动项目时先启动服务器再启动客户端，否则客户端无法连接到服务器。
* 端口号被占用，项目使用的是8888端口，如果端口被占用，可以自己更换。
* 在两台电脑上测试本项目时，两台电脑要连接同一个局域网（可以用手机的热点），并且更改ChatClient的connect方法中s = new Socket("127.0.0.1", 8888);
的参数IP为服务器电脑的IP。
* 所有文件编码均为gbk，如果出现乱码，请修改文件编码为gbk.
