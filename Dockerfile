FROM eclipse-temurin:17.0.6_10-jre

ARG VERSION

COPY build/libs/camunda-spring-boot-demo-${VERSION}.jar /opt/app.jar

CMD java \
  -Dspring.profiles.active=secure \
  -Dserver.port=${SERVER_PORT} \
  -Dauth.server.base.url=${AUTH_SERVER_BASE_URL} \
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
  -Djavax.net.ssl.trustStore=${JAVA_SSL_TRUSTSTORE} \
  -Djavax.net.ssl.trustStorePassword=${JAVA_SSL_TRUSTSTORE_PASSWORD} \
  -Dkafka.bootstrap.servers=${KAFKA_BOOTSTRAP_SERVERS} \
  -Dkafka.payment.topic=${KAFKA_PAYMENT_TOPIC} \
  -Dkafka.process.payment.group=${KAFKA_PROCESS_PAYMENT_GROUP} \
  -jar /opt/app.jar