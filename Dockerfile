# =========================
# Stage 1: Build del JAR
# =========================
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copia Maven wrapper e pom.xml
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Rendi mvnw eseguibile
RUN chmod +x mvnw

# Copia il codice sorgente
COPY src src

# Costruisci il JAR (senza test per velocizzare)
RUN ./mvnw clean package -DskipTests

# =========================
# Stage 2: Runtime
# =========================
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copia il JAR dal build stage
COPY --from=build /app/target/*.jar app.jar

# Esponi porta
EXPOSE 8080

# Variabile PORT di Render
ENV PORT=8080
ENV JAVA_OPTS=""

# Avvia l'app con la porta dinamica
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=$PORT -jar app.jar"]
