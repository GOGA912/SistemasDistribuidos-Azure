# Etapa 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/concurrency-service-1.0.0-jar-with-dependencies.jar app.jar

# Puerto para Cloud Run
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
