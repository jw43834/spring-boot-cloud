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

# 아키텍처
* registry - 
* config - 
* monitor - 
* zipkin - 
* gateway - 
* auth-service - 
* svca-service - 
* svcb-service - 

## 아키텍처
![architecture](/screenshots/architecture.jpg)
## 컴포넌트 구성
![components](/screenshots/components.jpg)


# 사용방법
* 기동 순서
    1. rabbitmq
    2. 호스트 파일 수정
       `127.0.0.1	registry config monitor rabbitmq auth-service`  
    3. registry、config、monitor、zipkin 기동
    4. 기동 gateway、auth-service、svca-service、svcb-service


# 프로젝트 화면

## Registry(Eureka)
http://localhost:8761/ id : user，암호 : password

![registry](/screenshots/registry.jpg)
## 모니터링(Spring Boot Admin)
http://localhost:8040/ id : admin，암호 : admin
### 어플리케이션 목록/상태
![monitor](/screenshots/monitor1.jpg)
### 어플리케이션 상태 이력
![monitor](/screenshots/monitor2.jpg)
### Turbine Hystrix
![monitor](/screenshots/monitor3.jpg)
### 서비스 상세 정보
![monitor](/screenshots/monitor4.jpg)
### 카운터
![monitor](/screenshots/monitor5.jpg)
### 환경 변수보기 수정
![monitor](/screenshots/monitor6.jpg)
### Logback 관리
![monitor](/screenshots/monitor7.jpg)
### JMX
![monitor](/screenshots/monitor8.jpg)
### 쓰레드
![monitor](/screenshots/monitor9.jpg)
### 인증이력
![monitor](/screenshots/monitor10.jpg)
### Http 이력
![monitor](/screenshots/monitor11.jpg)
### Hystrix
![monitor](/screenshots/monitor12.jpg)
## Zipkin
http://localhost:9411/ id : admin，암호 : admin
### 제어
![zipkin](/screenshots/zipkin1.jpg)
### 링크세부정보 추적
![zipkin](/screenshots/zipkin2.jpg)
### 서비스 Dependencies
![zipkin](/screenshots/zipkin3.jpg)
## RabbitMQ 관리자
http://localhost:15673/ guest/guest（rabbitmq 포트는 15672）

![rabbit](/screenshots/rabbit.jpg)

# Gateway 통한 API 호출 순서
1. Token 획득
```
curl -X POST -vu client:secret http://localhost:8060/uaa/oauth/token -H "Accept: application/json" -d "password=password&username=anil&grant_type=password&scope=read%20write"
```
응답결과：
```
{
  "access_token": "eac56504-c4f0-4706-b72e-3dc3acdf45e9",
  "token_type": "bearer",
  "refresh_token": "da1007dc-683c-4309-965d-370b15aa4aeb",
  "expires_in": 3599,
  "scope": "read write"
}
```
2. access token 사용하여 service a 호출
```
curl -i -H "Authorization: Bearer eac56504-c4f0-4706-b72e-3dc3acdf45e9" http://localhost:8060/svca
```
응답결과：
```
svca-service (172.18.0.8:8080)===>name:zhangxd
svcb-service (172.18.0.2:8070)===>Say Hello
```
3. access token 사용하여 service b 호출
```
curl -i -H "Authorization: Bearer eac56504-c4f0-4706-b72e-3dc3acdf45e9" http://localhost:8060/svcb
```
응답결과：
```
svcb-service (172.18.0.2:8070)===>Say Hello
```
4. refresh token 사용한 token 갱신
```
curl -X POST -vu client:secret http://localhost:8060/uaa/oauth/token -H "Accept: application/json" -d "grant_type=refresh_token&refresh_token=da1007dc-683c-4309-965d-370b15aa4aeb"
```
응답결과 Token：
```
{
  "access_token": "63ff57ce-f140-482e-ba7e-b6f29df35c88",
  "token_type": "bearer",
  "refresh_token": "da1007dc-683c-4309-965d-370b15aa4aeb",
  "expires_in": 3599,
  "scope": "read write"
}
```
5. 갱신
```
curl -X POST -vu user:password http://localhost:8888/bus/refresh
```
