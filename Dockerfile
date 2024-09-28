FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/security-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
