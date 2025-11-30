# --- ETAPA 1: Construcción (Build) ---
# Usamos una imagen base que tiene Java 21 y Maven
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiamos los archivos necesarios para descargar las librerías primero (para aprovechar la caché)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Copiamos todo el código fuente de tu proyecto
COPY src ./src

# Compilamos el proyecto y creamos el archivo .jar
RUN ./mvnw clean package -DskipTests

# --- ETAPA 2: Ejecución (Run) ---
# Usamos una imagen más ligera solo con Java para ejecutar la app
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Traemos el archivo .jar que creamos en la etapa 1
COPY --from=build /app/target/*.jar app.jar

# Creamos la carpeta para las imágenes
RUN mkdir -p /app/uploads/salas

# Indicamos que la app usa el puerto 8080
EXPOSE 8080

# Comando para arrancar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]