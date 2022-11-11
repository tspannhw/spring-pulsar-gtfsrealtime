## spring-pulsar-gtfsrealtime


Timothy Spann

Using the FLiPN Stack to ingest, route, enrich, transform, analyze, act on and display real-time transit information from various transit sources including MTA utilizings GTFS Real-Time feeds.


![Tim](https://raw.githubusercontent.com/tspannhw/spring-pulsar-gtfsrealtime/main/images/diagram.png)




### Using New Spring Module for Apache Pulsar

This uses https://github.com/spring-projects-experimental/spring-pulsar

### Setup

* IntelliJ with Spring Boot 3.0.0-RC1, Spring Pulsar 0.1.0-M1 and Java 17.0.4.13.0.0-RC1
* Apache Pulsar Version 2.10.1 works with 2.9.1+
* Set an environment variable with your api key code for your provider
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
        service-url: pulsar://localhost:6650
      producer:
        batching-enabled: false
        send-timeout-ms: 90000
        producer-name: airqualityspringboot
        topic-name: persistent://public/default/mtaalert

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

### Flink SQL

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


#### FLiPN-Stack References

* https://github.com/tspannhw/gtfs



#### Articles

* https://dev.to/gavinr/how-to-open-a-gtfs-bus-feed-in-the-browser-kgo

* 


#### Code

* https://codepen.io/gavinr/pen/oNXrYGw?editors=0010



#### Resources

* https://new.mta.info/developers

* https://api.mta.info/#/HelpDocument

* https://developers.google.com/protocol-buffers/

* https://github.com/OneBusAway/onebusaway-gtfs-realtime-api/blob/master/src/main/proto/com/google/transit/realtime/gtfs-realtime-NYCT.proto

* http://developer.onebusaway.org/modules/onebusaway-gtfs-realtime-api/current/

* https://api.mta.info/#/landing

* https://developers.google.com/transit/gtfs-realtime/

* https://api.mta.info/GTFS.pdf

* https://api.mta.info/#/HelpDocument

* https://github.com/protocolbuffers/protobuf/tree/2.7.0/examples

* http://bt.mta.info/wiki/Developers/Index

* http://bt.mta.info/wiki/Developers/GTFSRt

* http://bt.mta.info/wiki/Developers/SIRIIntro

* http://bt.mta.info/wiki/Developers/OneBusAwayRESTfulAPI

* http://developer.onebusaway.org/modules/onebusaway-application-modules/current/api/where/index.html

* http://web.mta.info/developers/

* http://mtadatamine.s3-website-us-east-1.amazonaws.com/#/subwayRealTimeFeeds

* https://github.com/google/transit/blob/master/gtfs-realtime/spec/en/trip-updates.md

* https://github.com/google/transit/blob/master/gtfs-realtime/spec/en/reference.md#message-stoptimeupdate

* https://github.com/google/transit/blob/master/gtfs/spec/en/reference.md#tripstxt

* https://github.com/OneBusAway/onebusaway/wiki

* https://developers.google.com/transit/gtfs-realtime/guides/trip-updates

* https://github.com/ktc312/NewYork_gtfs_realtime/

* https://developers.google.com/transit/gtfs-realtime/examples/java-sample

* https://github.com/OneBusAway/onebusaway-gtfs-realtime-api

* http://developer.onebusaway.org/modules/onebusaway-gtfs-realtime-api/current/

* https://gtfs.org/resources/

* https://github.com/cmoscardi/bus_kalman/blob/master/kalman.ipynb

* https://github.com/laidig/gtfs-rt-printer/blob/master/src/main/java/net/transitdata/gtfsrt/Main.java

* https://www.transit.land/feeds/

* https://www.transit.land/routes/r-dr4u-607#sources

* https://github.com/MobilityData/mobility-database-catalogs

* http://bustime.mta.info/wiki/Developers/GTFSRt

* https://github.com/FusionAuth/java-http

* https://github.com/google/transit

* https://github.com/google/transit/tree/master/gtfs-realtime/spec/en

* https://github.com/google/transit/blob/master/gtfs-realtime/spec/en/service-alerts.md

* https://github.com/google/transit/blob/master/gtfs-realtime/spec/en/trip-updates.md

* https://github.com/google/transit/blob/master/gtfs-realtime/spec/en/vehicle-positions.md

* https://gtfs.org/resources/data/

* https://github.com/jungckjp/subway-times/blob/ad9937820c41547eb126ccd900e4f6c4155e3aff/subway_times.py

* https://github.com/Cornell-Tech-Urban-Tech-Hub/BusObservatory-Grabber

* https://github.com/timescale/examples/blob/master/mta/gtfs-ingest.py

* https://github.com/DonRomaniello/InfoBoard/blob/main/resources/MTA_API.txt

* https://github.com/PeterJWei/CitywideFootprinting/tree/master/newWebService

* https://github.com/ktc312/NewYork_gtfs_realtime

* https://www.transit.land/feeds/f-bigbluebus~rt

* https://www.transit.land/feeds/f-smartbus~rt

* https://www.transit.land/feeds/f-nycferry~rt

* https://www.transit.land/feeds/f-mta~nyc~rt~alerts

* https://www.transit.land/feeds/f-mta~nyc~rt~subway~1~2~3~4~5~6~7

* https://www.transit.land/feeds/f-mta~nyc~rt~subway~a~c~e

* https://www.transit.land/feeds/f-mta~nyc~rt~subway~b~d~f~m

* https://www.transit.land/feeds/f-mta~nyc~rt~subway~g

* https://www.transit.land/feeds/f-mta~nyc~rt~subway~j~z

* https://www.transit.land/feeds/f-mta~nyc~rt~subway~l

* https://www.transit.land/feeds/f-mta~nyc~rt~subway~n~q~r~w

* https://www.transit.land/feeds/f-mta~nyc~rt~subway~sir

* https://www.transit.land/feeds/f-mtamaryland~marc~train~rt

* https://www.transit.land/feeds/f-hazleton~public~transit~rt

* https://github.com/camsys/onebusaway-nyc

* https://github.com/google/transit/tree/master/gtfs-realtime/spec/en

* https://github.com/tspannhw/gtfs_realtime_json




#### Subway Real-time Feeds MTA NYC (Registered Key required)

* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/nyct%2Fgtfs-ace
* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/nyct%2Fgtfs-g
* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/nyct%2Fgtfs-nqrw
* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/nyct%2Fgtfs
* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/nyct%2Fgtfs-bdfm
* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/nyct%2Fgtfs-jz
* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/nyct%2Fgtfs-l
* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/nyct%2Fgtfs-si


#### Other NYC MTA Feeds

* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/lirr%2Fgtfs-lirr
* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/mnr%2Fgtfs-mnr



#### Service Alert Feeds NYC MTA Using Header (x-api-key)

* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/camsys%2Fall-alerts
* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/camsys%2Fsubway-alerts
* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/camsys%2Fbus-alerts
* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/camsys%2Flirr-alerts
* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/camsys%2Fmnr-alerts

* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/camsys%2Fall-alerts.json
* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/camsys%2Fsubway-alerts.json
* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/camsys%2Fbus-alerts.json
* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/camsys%2Flirr-alerts.json
* https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/camsys%2Fmnr-alerts.json


#### Current MTA NYC Feeds

*  mtaalerts-url: http://gtfsrt.prod.obanyc.com/alerts?key=
*  vehiclepositions-url: http://gtfsrt.prod.obanyc.com/vehiclePositions?key=
*  tripupdates-url: http://gtfsrt.prod.obanyc.com/tripUpdates?key=



#### Example CURL

````
curl https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/camsys%2Fmnr-alerts.json -H "x-api-key: KeyIsLongSeeAccount" -H "Content-Type: application/json"

````



