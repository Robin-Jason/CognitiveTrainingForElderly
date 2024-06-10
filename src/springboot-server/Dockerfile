FROM openjdk:11-jre-slim

RUN mkdir -p /server
WORKDIR /server

COPY ./target/server-0.0.1-SNAPSHOT.jar app.jar

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms512m -Xmx512m -Djava.security.egd=file:/dev/./urandom"
ENV ARGS=""

EXPOSE 28080

CMD java ${JAVA_OPTS} -jar app.jar $ARGS