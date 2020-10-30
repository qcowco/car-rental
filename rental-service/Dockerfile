FROM openjdk:11-slim
EXPOSE 8091

WORKDIR /usr/src/app

COPY target/rental-service-0.0.1-SNAPSHOT.jar rental-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/usr/src/app/rental-service-0.0.1-SNAPSHOT.jar"]
