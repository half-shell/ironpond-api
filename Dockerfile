FROM openjdk:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/ironpond-api-0.0.1-SNAPSHOT-standalone.jar /ironpond-api/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/ironpond-api/app.jar"]
