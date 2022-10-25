## spring-pulsar-airquality


Timothy Spann

### New Spring Module for Apache Pulsar

This uses https://github.com/spring-projects-experimental/spring-pulsar

### Setup

* Visual Code with Spring Boot v3.0.0-M4 & Java 17.0.4.1
* Apache Pulsar Version 2.10.1 works with 2.9.1+
* Set an environment variable with your api key code from airnow
* Point to your Apache Pulsar cluster, if you are using StreamNative cloud I have SSL and configuration in the config class

### src/main/resources/application.yml

````

spring:
    pulsar:
      client:
#        service-url: pulsar+ssl://sn-academy.sndevadvocate.snio.cloud:6651
#        auth-plugin-class-name: org.apache.pulsar.client.impl.auth.oauth2.AuthenticationOAuth2
#        authentication:
#          issuer-url: https://auth.streamnative.cloud/
#          private-key: file:///Users/tspann/Downloads/sndevadvocate-tspann.json
#          audience: urn:sn:pulsar:sndevadvocate:my-instance
        service-url: pulsar://pulsar1:6650
      producer:
        batching-enabled: false
        send-timeout-ms: 90000
        producer-name: airqualityspringboot
        topic-name: persistent://public/default/airquality

logging:
  level:
    org.apache.pulsar: error
    root: info
    ROOT: info
    dev.datainmotion.gtfsrealtime: info

server.port: 8799   
````

### App Run

````

