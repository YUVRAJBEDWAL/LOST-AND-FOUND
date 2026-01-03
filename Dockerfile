 # Use Maven image to build
FROM maven:3.8.6-openjdk-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build the app
RUN mvn clean package -DskipTests

# Use OpenJDK runtime
FROM openjdk:17-jre-slim

WORKDIR /app

# Copy the built jar
COPY --from=build /app/target/campus-lost-found-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "app.jar"]
