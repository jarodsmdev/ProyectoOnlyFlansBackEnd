# Etapa 1: Compilación del proyecto (con tests)
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copia el descriptor de dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia el código fuente
COPY src ./src

# Compila sin ejecutar los tests
RUN mvn clean package -B -DskipTests

# Etapa 2: Ejecución de la aplicación
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia el artefacto generado
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto en el que se ejecutará la app
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]