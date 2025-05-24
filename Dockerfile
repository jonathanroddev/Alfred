# Build stage
FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /backoffice
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21.0.5_11-jdk
WORKDIR /backoffice
COPY --from=builder /backoffice/target/backoffice-0.0.1-SNAPSHOT.jar alfred.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "alfred.jar"]
