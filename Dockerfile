FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY target/happyhour-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app is listening on
EXPOSE 8080

# Run the Spring Boot application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
