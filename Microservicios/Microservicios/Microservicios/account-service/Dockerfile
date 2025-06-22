# Etapa 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Puerto expuesto para Cloud Run (espera que escuche en $PORT)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
