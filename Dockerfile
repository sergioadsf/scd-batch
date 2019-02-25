FROM openjdk:8-jdk-alpine

WORKDIR /app
COPY ./target/scd-batch-0.0.1-SNAPSHOT.jar /app
RUN mkdir /bck
EXPOSE 8082
VOLUME /bck:/home/sergio/Downloads
ENTRYPOINT java -jar scd-batch-0.0.1-SNAPSHOT.jar
