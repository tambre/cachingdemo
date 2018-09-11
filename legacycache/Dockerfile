FROM java
MAINTAINER tanmay ambre
COPY ./target/legacycache-0.0.1-SNAPSHOT.jar /usr/local/legacycache/
RUN ls -al /usr/local/legacycache
#EXPOSE 11023
VOLUME /logs
WORKDIR /usr/local/legacycache
ENTRYPOINT java -jar legacycache-0.0.1-SNAPSHOT.jar
