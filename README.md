## spring-pulsar-gtfsrealtime


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
2022-11-09T10:45:57.133-05:00  INFO 33974 --- [r-client-io-1-1] ealTimeProducerConfig$LoggingInterceptor : GTFS Producer: gtfsrtspringboot, MessageId: 14621:94:0, Key: d737c0e3-d686-49ed-bf50-3a65aac2fdca, Pub Time: 1668008757131, Schema: , Value: Alert[feedEntityID='MTABC_lmm:planned_work:6893', activePeriodStart=1665115233, activePeriodEnd=1683504000, headerText='Northbound Q25 stops on 7th Ave at 127th St and 125th St and on College Point Blvd at 7th Ave and Lax Ave are closed', headerLanguage='EN', agency='MTABC', tripRouteId='Q25', tripDirectionId=0, descriptionText='Northbound Q25 stops on 7th Ave at 127th St and 125th St and on College Point Blvd at 7th Ave and Lax Ave are closed
The last stop before detouring is 127th St at 9th Ave.

Buses make requested stops along 5th Ave from 127th St to College Point Blvd.

What's happening?
Con Edison work', descriptionLanguage='EN', effect='null', stopId='null', tripStartDate='null', tripStartTime='null', routeId='null', directionId=0, url='null', severityLevel='null', cause='null']
2022-11-09T10:46:01.186-05:00  INFO 33974 --- [ionShutdownHook] o.s.p.config.PulsarClientFactoryBean     : Closing client org.apache.pulsar.client.impl.PulsarClientImpl@15e3a063

----- got message -----
key:[09d013fd-5e36-4eff-b482-d4046312bdae], properties:[], content:{"feedEntityID":"MTABC_lmm:planned_work:5640","activePeriodStart":1659499200,"activePeriodEnd":1683504000,"headerText":"Northbound Q50 stop on Roosevelt Ave at Main St as been temporarily relocated to Main St at 39th Ave in front of GNC","headerLanguage":"EN","agency":"MTABC","tripRouteId":"Q50","tripDirectionId":0,"descriptionText":"Northbound Q50 stop on Roosevelt Ave at Main St as been temporarily relocated to Main St at 39th Ave in front of GNC\nWhat's happening?\nStaircase work at the Flushing-Main St Subway Station","descriptionLanguage":"EN","directionId":0}

````

**** Flink SQL

````
CREATE CATALOG pulsar WITH (
   'type' = 'pulsar-catalog',
   'catalog-service-url' = 'pulsar://localhost:6650',
   'catalog-admin-url' = 'http://localhost:8080'
);

SHOW CURRENT DATABASE;
SHOW DATABASES;

USE CATALOG pulsar;

set table.dynamic-table-options.enabled = true;

show databases;

use `public/default`;

show tables;

describe mtaalert;
+---------------------+--------+-------+-----+--------+-----------+
|                name |   type |  null | key | extras | watermark |
+---------------------+--------+-------+-----+--------+-----------+
|     activePeriodEnd | BIGINT | FALSE |     |        |           |
|   activePeriodStart | BIGINT | FALSE |     |        |           |
|              agency | STRING |  TRUE |     |        |           |
|               cause | STRING |  TRUE |     |        |           |
| descriptionLanguage | STRING |  TRUE |     |        |           |
|     descriptionText | STRING |  TRUE |     |        |           |
|         directionId | BIGINT | FALSE |     |        |           |
|              effect | STRING |  TRUE |     |        |           |
|        feedEntityID | STRING |  TRUE |     |        |           |
|      headerLanguage | STRING |  TRUE |     |        |           |
|          headerText | STRING |  TRUE |     |        |           |
|             routeId | STRING |  TRUE |     |        |           |
|       severityLevel | STRING |  TRUE |     |        |           |
|              stopId | STRING |  TRUE |     |        |           |
|     tripDirectionId | BIGINT | FALSE |     |        |           |
|         tripRouteId | STRING |  TRUE |     |        |           |
|       tripStartDate | STRING |  TRUE |     |        |           |
|       tripStartTime | STRING |  TRUE |     |        |           |
|                 url | STRING |  TRUE |     |        |           |
+---------------------+--------+-------+-----+--------+-----------+
19 rows in set

select agency, descriptionText, feedEntityID, headerText,  cause from mtaalert;

````
