FROM eclipse-temurin:21.0.6_7-jre

# 设置工作目录
WORKDIR /app

# 拷贝 target 目录下的 auth-client.jar 到容器的 /app 目录
COPY target/auth-client.jar /app/auth-client.jar

# 暴露环境变量
ENV SERVER_PORT 9999
ENV KNIFE4J_ENABLED true
ENV KNIFE4J_USERNAME numbfish
ENV KNIFE4J_PASSWORD numbfish
ENV MYSQL_HOST 127.0.0.1
ENV MYSQL_PORT 3306
ENV MYSQL_USERNAME root
ENV MYSQL_PASSWORD password
ENV REDIS_HOST 127.0.0.1
ENV REDIS_PORT 6379
ENV REDIS_PASSWORD password
ENV REDIS_DATABASE 0

# 暴露应用程序使用的端口，假设应用使用 9999 端口，根据实际情况修改
EXPOSE $SERVER_PORT

# 启动 Java 应用
CMD ["java", "-jar", "auth-client.jar"]