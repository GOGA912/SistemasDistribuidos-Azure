# Etapa 1: Construcción del JAR con Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final para ejecutar
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY Banco.db Banco.db
EXPOSE 8081 8082 8083 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
