FROM adoptopenjdk/openjdk15:alpine-jre

ARG VERSION

COPY build/libs/camunda-spring-boot-demo-${VERSION}.jar /opt/app.jar

CMD java \
  -Dspring.profiles.active=secure \
  -Dserver.port=${SERVER_PORT} \
  -Dauth.server.http=${AUTH_SERVER_HTTP} \
  -Dauth.server.https=${AUTH_SERVER_HTTPS} \
  -Dauth.realm=${AUTH_REALM} \
  -Dauth.client.id=${AUTH_CLIENT_ID} \
  -Dauth.client.secret=${AUTH_CLIENT_SECRET} \
  -Dauth.admin.group=${AUTH_ADMIN_GROUP} \
  -Dquote.host=${QUOTE_HOST} \
  -Daws.accessKeyId=${AWS_ACCESS_KEY_ID} \
  -Daws.secretAccessKey=${AWS_SECRET_ACCESS_KEY} \
  -Daws.region=${AWS_REGION} \
  -Daws.s3.endpoint.override=${AWS_S3_ENDPOINT_OVERRIDE} \
  -Daws.s3.payment.bucket.name=${AWS_S3_PAYMENT_BUCKET_NAME} \
  -jar /opt/app.jar