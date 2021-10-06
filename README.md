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

```gradle
// cleans build directories
// prints currentVersion
// formats code
// builds code
// runs tests
// checks for gradle issues
// checks dependency versions
./gradlew clean currentVersion dependencyUpdates lintGradle spotlessApply build
```