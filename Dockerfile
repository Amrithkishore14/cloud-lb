# ==========================
#  CLOUD LOAD BALANCER APP
# ==========================
FROM eclipse-temurin:17-jdk

# Set work directory
WORKDIR /app

# Copy Maven files
COPY pom.xml /app/
COPY src /app/src

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Build the project
RUN mvn clean package -Dmaven.test.skip=true

# Copy SQLite DB if exists(ignore if missing)
RUN touch /app/database.db

# Expose JavaFX display (optional if using GUI forwarding)
ENV DISPLAY=:0

# Run the JAR
CMD ["java", "-jar", "target/cloud-lb-1.0-SNAPSHOT.jar"]

