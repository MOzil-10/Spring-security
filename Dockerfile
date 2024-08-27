FROM maven:3.9.4-openjdk-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/security-application.jar /app/security-application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "security-application.jar"]
