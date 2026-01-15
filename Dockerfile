# Dockerfile
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY /gameapp/target/game-server.jar app.jar

EXPOSE 14527

ENTRYPOINT ["java","-jar","app.jar"]
