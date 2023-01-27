# Camunda Spring Boot Demo

[![Build](https://github.com/michaelruocco/camunda-spring-boot-demo/workflows/pipeline/badge.svg)](https://github.com/michaelruocco/camunda-spring-boot-demo/actions)
[![codecov](https://codecov.io/gh/michaelruocco/camunda-spring-boot-demo/branch/master/graph/badge.svg?token=FWDNP534O7)](https://codecov.io/gh/michaelruocco/camunda-spring-boot-demo)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/272889cf707b4dcb90bf451392530794)](https://www.codacy.com/gh/michaelruocco/camunda-spring-boot-demo/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=michaelruocco/camunda-spring-boot-demo&amp;utm_campaign=Badge_Grade)
[![BCH compliance](https://bettercodehub.com/edge/badge/michaelruocco/camunda-spring-boot-demo?branch=master)](https://bettercodehub.com/)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_camunda-spring-boot-demo&metric=alert_status)](https://sonarcloud.io/dashboard?id=michaelruocco_camunda-spring-boot-demo)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_camunda-spring-boot-demo&metric=sqale_index)](https://sonarcloud.io/dashboard?id=michaelruocco_camunda-spring-boot-demo)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_camunda-spring-boot-demo&metric=coverage)](https://sonarcloud.io/dashboard?id=michaelruocco_camunda-spring-boot-demo)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_camunda-spring-boot-demo&metric=ncloc)](https://sonarcloud.io/dashboard?id=michaelruocco_camunda-spring-boot-demo)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.michaelruocco/camunda-spring-boot-demo.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.michaelruocco%22%20AND%20a:%22camunda-spring-boot-demo%22)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Overview

This repo contains a simple demo application using [Camunda](https://camunda.com/) embedded inside spring boot.

## Useful Commands

```bash
// cleans build directories
// prints currentVersion
// formats code
// builds code
// runs tests
// checks for gradle issues
// checks dependency versions
./gradlew clean currentVersion dependencyUpdates spotlessApply build integrationTest
```

## Running locally with dependencies and app on local machien

Run the following command to spin up dependencies

```bash
docker-compose up -d
```

Then run the following command to run the application on your local machine
configured to talk to the dependencies running inside docker compose:

```bash
./gradlew bootRun -Dspring.profiles.active=secure \
  -Dserver.port=8090 \
  -Dauth.server.base.url=https://localhost:8092 \
  -Dauth.realm=demo-local \
  -Dauth.client.id=demo-client-id \
  -Dauth.client.secret=demo-client-secret \
  -Dauth.admin.group=camunda-admin \
  -Dquote.host=http://localhost:8093 \
  -Daws.accessKeyId=abc \
  -Daws.secretAccessKey=123 \
  -Daws.region=eu-west-2 \
  -Daws.s3.endpoint.override=http://s3.localhost.localstack.cloud:4566 \
  -Daws.s3.payment.bucket.name=demo-payment \
  -Djavax.net.ssl.trustStore=$(pwd)/keycloak/certs/truststore.jks \
  -Djavax.net.ssl.trustStorePassword=changeit \
  -Dkafka.bootstrap.servers=http://localhost:9094 \
  -Dkafka.payment.topic=payment-topic \
  -Dkafka.process.payment.group=process-payment-group
```

## Running locally with dependencies and app in docker

```bash
./gradlew clean spotlessApply build integrationTest buildImage composeUp
```

Once the service is running locally you can attempt to log in [here](http://localhost:8083). You
can log in using either the admin credentials of username `demo-admin` with password `welcomeAdmin01`
or the standard user credentials `demo-user` with password `welcome01`. However, you will find that the
communication between the web application and keycloak to not work correctly unless you make a tweak
to your local `/etc/hosts` file and enter the following line:

```bash
127.0.0.1   keycloak
```

Once this entry is added to your `/etc/hosts` is updated the login flow should work as expected.

## Running locally without dependencies

Since it can take a while for the docker dependency containers (particularly keycloak) to start up,
it is also possible to run the service locally with all external dependencies stubbed
out from within the application.

In this case instead of writing to S3 the json that would be written to S3 is just logged instead.

There is no authentication provider, so to log into the admin console you should use the default admin
user which is both user id: `demo` and password: `demo`.

Instead of calling the quote API to retrieve a random quote, a stubbed quote is created which
includes a random UUID for uniqueness.

To do this you can run the following command:

```bash
./gradlew bootRun -Dserver.port=8090 \
  -Dspring.profiles.active=stubbed \
  -Dspring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration
```

## Running Postman integration tests with dependencies

```bash
./gradlew clean bootJar buildImage composeUp postman composeDown
```

This will build the application into a docker container and then runs the application container
alongside a keycloak container which supports security and authentication.