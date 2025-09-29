# Use a base image with Java
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy the JAR file into the container
COPY target/api-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8090

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
