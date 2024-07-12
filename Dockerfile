# Usa un'immagine di base di OpenJDK
FROM openjdk:17-jdk-slim

# Imposta il directory di lavoro
WORKDIR /app

# Copia il file JAR dell'applicazione nel container
COPY target/PricePharmacyProducts-0.0.1-SNAPSHOT.jar app.jar

# Esponi la porta su cui l'applicazione Spring Boot è in ascolto
EXPOSE 8080

# Comando per eseguire l'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]
