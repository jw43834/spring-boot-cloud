# Spring Cloud
 Spring Boot, Spring Cloud, Spring Oauth2, Spring Cloud Netflix를 적용하여 개발
 기존 마스터 프로젝트에서 스프링 부트, 클라우드 버전업

# 구성
* Spring boot - 
* Eureka - 
* Spring Cloud Config - 
* Hystrix - 
* Zuul - 
* Spring Cloud Bus - 
* Spring Cloud Sleuth
* Ribbon
* Turbine
* Spring Cloud Stream
* Feign
* Spring Cloud OAuth2

# 应用架构

该项目包含 8 个服务

* registry - 服务注册与发现
* config - 外部配置
* monitor - 监控
* zipkin - 分布式跟踪
* gateway - 代理所有微服务的接口网关
* auth-service - OAuth2 认证服务
* svca-service - 业务服务A
* svcb-service - 业务服务B

## 体系架构
![architecture](/screenshots/architecture.jpg)
## 应用组件
![components](/screenshots/components.jpg)

# 启动项目

* 使用 Docker 快速启动
    1. 配置 Docker 环境
    2. `mvn clean package` 打包项目及 Docker 镜像
    3. 在项目根目录下执行 `docker-compose up -d` 启动所有项目
* 本地手动启动
    1. 配置 rabbitmq
    2. 修改 hosts 将主机名指向到本地   
       `127.0.0.1	registry config monitor rabbitmq auth-service`  
       或者修改各服务配置文件中的相应主机名为本地 ip
    3. 启动 registry、config、monitor、zipkin
    4. 启动 gateway、auth-service、svca-service、svcb-service

# 项目预览

## 注册中心
访问 http://localhost:8761/ 默认账号 user，密码 password

![registry](/screenshots/registry.jpg)
## 监控
访问 http://localhost:8040/ 默认账号 admin，密码 admin
### 控制面板
![monitor](/screenshots/monitor1.jpg)
### 应用注册历史
![monitor](/screenshots/monitor2.jpg)
### Turbine Hystrix面板
![monitor](/screenshots/monitor3.jpg)
### 应用信息、健康状况、垃圾回收等详情
![monitor](/screenshots/monitor4.jpg)
### 计数器
![monitor](/screenshots/monitor5.jpg)
### 查看和修改环境变量
![monitor](/screenshots/monitor6.jpg)
### 管理 Logback 日志级别
![monitor](/screenshots/monitor7.jpg)
### 查看并使用 JMX
![monitor](/screenshots/monitor8.jpg)
### 查看线程
![monitor](/screenshots/monitor9.jpg)
### 认证历史
![monitor](/screenshots/monitor10.jpg)
### 查看 Http 请求轨迹
![monitor](/screenshots/monitor11.jpg)
### Hystrix 面板
![monitor](/screenshots/monitor12.jpg)
## 链路跟踪
访问 http://localhost:9411/ 默认账号 admin，密码 admin
### 控制面板
![zipkin](/screenshots/zipkin1.jpg)
### 链路跟踪明细
![zipkin](/screenshots/zipkin2.jpg)
### 服务依赖关系
![zipkin](/screenshots/zipkin3.jpg)
## RabbitMQ 监控
Docker 启动访问 http://localhost:15673/ 默认账号 guest，密码 guest（本地 rabbit 管理系统默认端口15672）

![rabbit](/screenshots/rabbit.jpg)
# 接口测试
1. 获取 Token
```
curl -X POST -vu client:secret http://localhost:8060/uaa/oauth/token -H "Accept: application/json" -d "password=password&username=anil&grant_type=password&scope=read%20write"
```
返回如下格式数据：
```
{
  "access_token": "eac56504-c4f0-4706-b72e-3dc3acdf45e9",
  "token_type": "bearer",
  "refresh_token": "da1007dc-683c-4309-965d-370b15aa4aeb",
  "expires_in": 3599,
  "scope": "read write"
}
```
2. 使用 access token 访问 service a 接口
```
curl -i -H "Authorization: Bearer eac56504-c4f0-4706-b72e-3dc3acdf45e9" http://localhost:8060/svca
```
返回如下数据：
```
svca-service (172.18.0.8:8080)===>name:zhangxd
svcb-service (172.18.0.2:8070)===>Say Hello
```
3. 使用 access token 访问 service b 接口
```
curl -i -H "Authorization: Bearer eac56504-c4f0-4706-b72e-3dc3acdf45e9" http://localhost:8060/svcb
```
返回如下数据：
```
svcb-service (172.18.0.2:8070)===>Say Hello
```
4. 使用 refresh token 刷新 token
```
curl -X POST -vu client:secret http://localhost:8060/uaa/oauth/token -H "Accept: application/json" -d "grant_type=refresh_token&refresh_token=da1007dc-683c-4309-965d-370b15aa4aeb"
```
返回更新后的 Token：
```
{
  "access_token": "63ff57ce-f140-482e-ba7e-b6f29df35c88",
  "token_type": "bearer",
  "refresh_token": "da1007dc-683c-4309-965d-370b15aa4aeb",
  "expires_in": 3599,
  "scope": "read write"
}
```
5. 刷新配置
```
curl -X POST -vu user:password http://localhost:8888/bus/refresh
```
