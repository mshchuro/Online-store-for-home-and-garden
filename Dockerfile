FROM maven:3.9.9 AS build
WORKDIR /app

COPY  pom.xml .
RUN mvn dependency:go-offline
COPY src/ ./src
RUN mvn clean package -DskipTests



FROM openjdk:17
WORKDIR /app

COPY --from=build /app/target/online-store-1.0.0-SNAPSHOT.jar /app/online-store-1.0.0-SNAPSHOT.jar

CMD ["java", "-jar","online-store-1.0.0-SNAPSHOT.jar"]