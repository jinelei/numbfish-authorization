# Numbfish Auth 授权模块

## Docker

1. install整个numbfish-auth项目

``./mvnw install -f ./pom.xml``

2. package整个numbfish-auth项目

``./mvnw package -f ./pom.xml``

3. 切换到auth-client下，执行docker build构建镜像

``cd auth-client``  
``docker build --rm -t numbfish-auth .``

### Docker 暴露环境变量说明

|变量名称|默认值|说明|
|:---|:---|:---|
|SERVER_PORT|9999|服务端口|
|KNIFE4J_ENABLED|true|是否开启knife4j|
|KNIFE4J_USERNAME|numbfish|knife4j 用户名|
|KNIFE4J_PASSWORD|numbfish|knife4j 密码|
|MYSQL_HOST|127.0.0.1|mysql 地址|
|MYSQL_PORT|3306|mysql 端口|
|MYSQL_USERNAME|root|mysql 用户名|
|MYSQL_PASSWORD|iotG@2025|mysql 密码|
|REDIS_HOST|127.0.0.1|redis 地址|
|REDIS_PORT|6379|redis 端口|
|REDIS_PASSWORD|iotG@2025|redis 密码|
|REDIS_DATABASE|0|redis 数据库|
