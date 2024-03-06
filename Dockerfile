FROM openjdk:20
EXPOSE 8081
WORKDIR /web
ADD target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

