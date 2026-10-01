FROM maven:3.9.16-eclipse-temurin-17 AS base

WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src

FROM base AS test
RUN mvn -B clean verify

FROM scratch AS test-report
COPY --from=test /app/target/site/jacoco /jacoco
COPY --from=test /app/target/surefire-reports /surefire-reports

FROM base AS build
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
