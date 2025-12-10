FROM maven:3.9-openjdk-17 as build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -q -DskipTests clean package

FROM openjdk:17-jre

WORKDIR /app

COPY --from=build /app/target/bookings-service-1.0.0.jar bookings-service.jar

EXPOSE 8093

ENTRYPOINT ["java", "-jar", "/app/bookings-service.jar"]

