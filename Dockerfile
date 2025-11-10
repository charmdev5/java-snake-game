# ✅ Use the universal Corretto JDK 17 image that works on both Mac & GCP
FROM amazoncorretto:17-alpine

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/snake-game-1.0.jar app.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
