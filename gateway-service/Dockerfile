FROM openjdk:11-slim
EXPOSE 8082

WORKDIR /usr/src/app

COPY target/gateway-service-0.0.1-SNAPSHOT.jar gateway-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/usr/src/app/gateway-service-0.0.1-SNAPSHOT.jar"]
