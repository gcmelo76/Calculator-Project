FROM maven:3.9.7-eclipse-temurin-21-alpine AS builder

WORKDIR /home/app
ADD . /home/app/Calculator
RUN cd Calculator && mvn clean package


FROM eclipse-temurin:21-jre-alpine

WORKDIR /home/app
EXPOSE 8080
COPY --from=builder /home/app/Calculator/target/*.jar calculator-1.0.0.jar
ENTRYPOINT ["java", "-jar", "calculator-1.0.0.jar"]