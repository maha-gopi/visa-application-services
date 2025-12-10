FROM maven:3.9-openjdk-17 as build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -q -DskipTests clean package

FROM openjdk:17-jre

WORKDIR /app

COPY --from=build /app/target/visa-types-service-1.0.0.jar visa-types-service.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "/app/visa-types-service.jar"]


