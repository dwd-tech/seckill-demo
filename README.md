这是一个简单的基于spring boot的一个秒杀系统的项目以及测试
项目环境以及对应的数据版本均在docker里的配置文件中.
 1. 环境准备
在运行项目前，请确保本地已安装以下环境：
- [JDK 21]
- [Maven 3.9]
- [Docker 4.78]
2. 一键启动中间件 (Docker)
项目根目录下提供了配置好的 Docker 环境。通过以下命令一键启动 MySQL、Redis 及 RocketMQ等

进入 docker 目录
cd docker
后台启动所有依赖服务
docker-compose up -d
启动后，会在本地生成对应的挂载数据目录。

3. 初始化数据库
使用客户端（Navicat 或 DBeaver）连接本地 MySQL：
端口：3306 
用户名/密码：root / root  ,可以在配置文件application.yml中修改
运行项目中 sql/ 目录下的初始化脚本（如有），创建数据库 seckill 及商品表、订单表。

4.运行
在IDEA中运行SeckillApplication.java运行成功通过浏览器进入  http://localhost:8080/  后可以看到以下的测试界面
<img width="1815" height="1242" alt="QQ_1781597722257" src="https://github.com/user-attachments/assets/2ab57be4-b093-4ae6-8f7e-938fd78d4364" />
