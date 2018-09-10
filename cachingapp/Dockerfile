FROM java
MAINTAINER tanmay ambre
COPY ./target/cachingapp-0.0.1-SNAPSHOT.jar /usr/local/cachingapp/
RUN ls -al /usr/local/cachingapp
EXPOSE 11021
VOLUME /logs
WORKDIR /usr/local/cachingapp
ENTRYPOINT java -jar cachingapp-0.0.1-SNAPSHOT.jar
