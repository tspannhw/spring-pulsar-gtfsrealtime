package dev.datainmotion.gtfsrealtime;

import dev.datainmotion.gtfsrealtime.model.Alert;
import dev.datainmotion.gtfsrealtime.service.GTFSRealTimeService;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.UUID;

/**
 * example spring boot app to read GTFS real-time transit feed send to Pulsar
 */

@EnableScheduling
@SpringBootApplication
public class GTFSRealTimeApp {
	private static final Logger log = LoggerFactory.getLogger(GTFSRealTimeApp.class);
	public static final String ERRORMSG1 = "No GTFS data found or failed or outrun our 500 allotment.";

	@Autowired
    private GTFSRealTimeService gtfsRealTimeService;

	@Autowired
	private PulsarTemplate<Alert> pulsarTemplate;

    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(GTFSRealTimeApp.class, args);
    }

	@Scheduled(initialDelay = 10, fixedRate = 10000)
    public void getRows() {
		//List<Alert> list = gtfsRealTimeService.getMTAVehiclePositions();
       // gtfsRealTimeService.getMTATripUpdates();

        List<Alert> alerts = gtfsRealTimeService.getMTAAlerts();
        if (alerts == null || alerts.size() <= 0) {
			log.debug(ERRORMSG1);
            return;
        }
        log.debug("Count: {}", alerts.size());
		this.pulsarTemplate.setSchema(Schema.JSON(Alert.class));

        alerts.forEach((alert) -> {
            if ( alert != null &&
                    alert.getHeaderText() != null &&
                alert.getAgency() != null &&
                alert.getDescriptionText() != null)
            {
                log.debug("Alert FeedEntityID:{} Agency:{} => Descr:{}, Header: {}",
                        alert.getFeedEntityID(),
                        alert.getAgency(),
                        alert.getDescriptionText().substring(0, 25),
                        alert.getHeaderText().substring(0, 25)
                );
            }
			try {
				UUID uuidKey = UUID.randomUUID();
                System.out.println("Alert object: " + alert.toString());
				MessageId msgid = pulsarTemplate.newMessage(alert)
						.withMessageCustomizer((mb) -> mb.key(uuidKey.toString()))
						.send();
				log.debug("MSGID Sent: {}", msgid.toString());
			}
			catch (Throwable e) {
				log.error("Pulsar Error", e);
			}
		});
    }

//	@PulsarListener(subscriptionName = "aq-spring-reader", subscriptionType = Shared, schemaType = SchemaType.JSON, topics = "persistent://public/default/aq-pm25")
//	void echoObservation(Observation message) {
//		this.log.info("PM2.5 Message received: {}", message);
//	}
}