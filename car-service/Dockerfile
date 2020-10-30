FROM openjdk:11-slim
EXPOSE 8090

WORKDIR /usr/src/app

COPY target/car-service-0.0.1-SNAPSHOT.jar car-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/usr/src/app/car-service-0.0.1-SNAPSHOT.jar"]
