# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the application JAR file
COPY target/vehiclemanager-1.0.1.jar /app/vehiclemanager.jar

# Expose the port that the application will run on
EXPOSE 8080

# Set JAVA_OPTS environment variable
ENV JAVA_OPTS="-Xms200m -Xmx2g -XX:MetaspaceSize=192M -XX:MaxMetaspaceSize=512m"

# Run the application with JAVA_OPTS
CMD ["sh", "-c", "java ${JAVA_OPTS} -jar /app/vehiclemanager.jar"]