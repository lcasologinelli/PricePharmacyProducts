# Stage 1: build del JAR
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copia Maven wrapper e pom.xml
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Copia il codice sorgente
COPY src src

# Costruisci il JAR (senza test per velocizzare)
RUN ./mvnw clean package -DskipTests

# Stage 2: runtime
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copia il JAR dal build stage
COPY --from=build /app/target/*.jar app.jar

# Esponi porta
EXPOSE 8080

# Avvia l'app
ENTRYPOINT ["java", "-jar", "app.jar"]
