FROM openjdk:11-slim
EXPOSE 8099

WORKDIR /usr/src/app

COPY target/user-service-0.0.1-SNAPSHOT.jar user-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/usr/src/app/user-service-0.0.1-SNAPSHOT.jar"]
