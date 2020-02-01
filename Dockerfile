FROM openjdk:8-alpine
RUN apk add curl
WORKDIR /app
COPY target/mockyup-0.0.1-SNAPSHOT.jar /app/app.jar
HEALTHCHECK --timeout=5s --interval=10s --retries=3 \
  CMD curl -s --fail http://localhost:$SERVER_PORT/health || exit 1
ENTRYPOINT ["java", "-jar","app.jar"]
