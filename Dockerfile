FROM eclipse-temurin:21-jdk
LABEL authors="Kishore"

WORKDIR /app
COPY target/atlas.jar  atlas.jar
EXPOSE 8080


ENTRYPOINT ["java", "-jar", "atlas.jar"]