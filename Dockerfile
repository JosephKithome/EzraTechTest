FROM openjdk:17-jdk-slim-bullseye

WORKDIR /app

COPY build/libs/EzraLendingApi-0.0.1-SNAPSHOT.jar .

EXPOSE 8091

CMD ["java", "-jar", "EzraLendingApi-0.0.1-SNAPSHOT.jar"]
