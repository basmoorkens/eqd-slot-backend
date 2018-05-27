FROM openjdk:8-jdk-alpine
VOLUME /tmp
RUN mkdir /eqd
COPY  ./target/slots-0.0.2-SNAPSHOT.jar /eqd/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/eqd/app.jar"]
