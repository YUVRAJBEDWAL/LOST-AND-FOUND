 # Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built JAR file
COPY target/campus-lost-found-1.0.0.jar app.jar

# Expose port 8080
EXPOSE 8080

# Add wait script for database dependency
RUN apt-get update && apt-get install -y wget && rm -rf /var/lib/apt/lists/*

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/auth/me || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
