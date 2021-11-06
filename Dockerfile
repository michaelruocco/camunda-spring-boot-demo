FROM adoptopenjdk/openjdk15:alpine-jre

ARG VERSION

ENV SERVER_PORT=80 \
    AUTH_SERVER_URL=http://localhost:8080/auth \
    AUTH_REALM=demo-local \
    AUTH_CLIENT_ID=demo-client-id \
    AUTH_CLIENT_SECRET=demo-client-secret

COPY build/libs/camunda-spring-boot-demo-${VERSION}.jar /opt/app.jar

CMD java \
  -Dserver.port=${SERVER_PORT} \
  -Dauth.server.url=${AUTH_SERVER_URL} \
  -Dauth.realm=${AUTH_REALM} \
  -Dauth.client.id=${AUTH_CLIENT_ID} \
  -Dauth.client.secret=${AUTH_CLIENT_SECRET} \
  -jar /opt/app.jar