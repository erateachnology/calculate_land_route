FROM openjdk:17
EXPOSE 9000
ARG JAR_FILE=target/routing_service-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]