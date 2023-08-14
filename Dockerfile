FROM eclipse-temurin:17-jdk-alpine

ARG JAR_FILE=target/*.jar

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY ${JAR_FILE} app.jar

# Expose the port your Spring Boot app is listening on
EXPOSE 8080

# Run the Spring Boot application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
