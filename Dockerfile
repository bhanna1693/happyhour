FROM eclipse-temurin:17.0.8_7-jre-ubi9-minimal

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY target/happyhour-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app is listening on
EXPOSE 8080

# Run the Spring Boot application when the container starts
CMD ["java", "-jar", "app.jar"]
