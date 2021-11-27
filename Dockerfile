FROM adoptopenjdk/openjdk15:alpine-jre

ARG VERSION

ENV SERVER_PORT=80 \
    AUTH_REALM=demo-local \
    AUTH_CLIENT_ID=demo-client-id \
    AUTH_CLIENT_SECRET=demo-client-secret \
    AUTH_ADMIN_GROUP=camunda-admin

COPY build/libs/camunda-spring-boot-demo-${VERSION}.jar /opt/app.jar

CMD java \
  -Dserver.port=${SERVER_PORT} \
  -Dauth.server.http=${AUTH_SERVER_HTTP} \
  -Dauth.server.https=${AUTH_SERVER_HTTPS} \
  -Dauth.realm=${AUTH_REALM} \
  -Dauth.client.id=${AUTH_CLIENT_ID} \
  -Dauth.client.secret=${AUTH_CLIENT_SECRET} \
  -Dauth.admin.group=${AUTH_ADMIN_GROUP} \
  -Dquote.host=${QUOTE_HOST} \
  -jar /opt/app.jar