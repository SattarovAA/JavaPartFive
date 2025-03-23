FROM openjdk:17-oracle
WORKDIR /app
COPY . /app
COPY target/KafkaAuth-1.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]