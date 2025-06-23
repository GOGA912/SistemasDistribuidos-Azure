# Etapa de construcción
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar todo el proyecto
COPY . .

# Construir el proyecto
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiar el jar generado
COPY --from=build /app/target/movements-service-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto (opcional para documentación)
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
