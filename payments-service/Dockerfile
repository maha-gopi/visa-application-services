FROM maven:3.9-amazoncorretto-17 as build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -q -DskipTests clean package

FROM amazoncorretto:17

WORKDIR /app

COPY --from=build /app/target/payments-service-1.0.0.jar payments-service.jar

EXPOSE 8092

ENTRYPOINT ["java", "-jar", "/app/payments-service.jar"]

