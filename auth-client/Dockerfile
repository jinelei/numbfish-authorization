FROM openjdk:17-jre

# 设置工作目录
WORKDIR /app

# 拷贝 target 目录下的 auth-client.jar 到容器的 /app 目录
COPY target/auth-client.jar /app/auth-client.jar

# 暴露应用程序使用的端口，假设应用使用 9999 端口，根据实际情况修改
EXPOSE 9999

# 启动 Java 应用
CMD ["java", "-jar", "app/auth-client.jar"]