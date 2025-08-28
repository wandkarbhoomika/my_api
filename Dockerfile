# Use OpenJDK 17 image as base
FROM openjdk:17-jdk-slim

# Create working directory inside container
WORKDIR /app

# Copy the JAR file from target folder to container
COPY target/api-0.0.1-SNAPSHOT.jar app.jar

#Spring Boot app runs on , port number
EXPOSE 8010

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]
