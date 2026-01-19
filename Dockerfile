FROM eclipse-temurin:25-jdk

WORKDIR /app
COPY target/atlas.jar  atlas.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "atlas.jar"]