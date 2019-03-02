FROM frolvlad/alpine-oraclejdk8:slim

ARG JAR_FILE

VOLUME /tmp
ADD ${JAR_FILE} app.jar

RUN addgroup bootapp && \
    adduser -D -S -h /var/cache/bootapp -s /sbin/nologin -G bootapp bootapp

RUN sh -c 'touch /app.jar'
RUN apk add --no-cache py-pip python jq && pip install awscli
USER bootapp
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar app.jar
