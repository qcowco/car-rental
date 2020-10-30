FROM openjdk:11-slim
EXPOSE 8761

WORKDIR /usr/src/app

COPY target/discovery-service-0.0.1-SNAPSHOT.jar discovery-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/usr/src/app/discovery-service-0.0.1-SNAPSHOT.jar"]
