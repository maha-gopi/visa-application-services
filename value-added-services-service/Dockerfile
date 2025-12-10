FROM maven:3.9-amazoncorretto-17 as build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -q -DskipTests clean package

FROM amazoncorretto:17

WORKDIR /app

COPY --from=build /app/target/value-added-services-service-1.0.0.jar value-added-services-service.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/app/value-added-services-service.jar"]

