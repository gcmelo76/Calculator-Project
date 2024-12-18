FROM maven:3.9.7-eclipse-temurin-21-alpine AS builder

WORKDIR /home/app
ADD . /home/app/Rest
RUN cd Rest && mvn clean package


FROM eclipse-temurin:21-jre-alpine

WORKDIR /home/app
EXPOSE 8080
COPY --from=builder /home/app/Rest/target/*.jar rest-1.0.0.jar
ENTRYPOINT ["java", "-jar", "rest-1.0.0.jar"]