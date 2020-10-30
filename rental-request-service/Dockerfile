FROM openjdk:11-slim
EXPOSE 8092

WORKDIR /usr/src/app

COPY target/rental-request-service-0.0.1-SNAPSHOT.jar rental-request-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/usr/src/app/rental-request-service-0.0.1-SNAPSHOT.jar"]
