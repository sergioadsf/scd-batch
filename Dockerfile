FROM openjdk:8-jdk-alpine

WORKDIR .
#COPY ./target/scd-batch-0.0.1-SNAPSHOT.jar /app.jar
RUN mkdir /bck
EXPOSE 8082
#VOLUME /bck:/home/sergio/Downloads
ENTRYPOINT java -jar app.jar
