FROM maven:3.6.3-ibmjava-8-alpine AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml package -Ptest

FROM openjdk:8-alpine
COPY --from=build /home/app/target/mockyup-RELEASE.jar /usr/app/mockyup-RELEASE.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/mockyup-RELEASE.jar"]
