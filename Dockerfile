# Dockerfile
FROM swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/openjdk:21

WORKDIR /app

COPY /gameapp/target/game-server.jar app.jar

EXPOSE 14527

ENTRYPOINT ["java","-jar","app.jar"]
