# Etapa de construcción (Build Stage)
# Usamos una imagen de Maven con Java 17 para construir el proyecto
FROM maven:3.8-openjdk-17 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos primero el pom.xml para aprovechar el caché de Docker
COPY pom.xml .

# Copiamos el resto del código fuente del proyecto
COPY src ./src

# Ejecutamos el comando de Maven para construir el archivo .jar
# Esto descargará las dependencias y compilará el código
RUN mvn clean package -DskipTests

# Etapa de ejecución (Run Stage)
# Usamos una imagen ligera de Java 17 para ejecutar la aplicación
FROM openjdk:17-jdk-slim

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el archivo .jar que se construyó en la etapa anterior
# OJO: Este nombre de archivo debe coincidir con el de tu pom.xml (artifactId-version.jar)
COPY --from=build /app/target/api-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto 8080 (el puerto por defecto de Spring Boot)
EXPOSE 8080

# El comando para iniciar la aplicación cuando el contenedor se ejecute
ENTRYPOINT ["java", "-jar", "app.jar"]