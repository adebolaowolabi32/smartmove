FROM openjdk:11-jre-slim

ENV TZ Africa/Lagos

LABEL maintainer="adebola.owolabi@interswitchgroup.com"

ADD target/smartmove.jar /opt/smartmove.jar

COPY resources /opt/resources

WORKDIR /opt

CMD ["java", "-jar", "smartmove.jar"]