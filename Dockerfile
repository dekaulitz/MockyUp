FROM openjdk:8-alpine
WORKDIR /app
COPY target/mockyup-0.0.1-SNAPSHOT.jar /app/app.jar
CMD ["java", "-jar","app.jar"]
