FROM maven:3.9.6-openjdk-17 as builder
WORKDIR /app
COPY . /app/.
COPY ./compose.yml /app/compose.yml
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]