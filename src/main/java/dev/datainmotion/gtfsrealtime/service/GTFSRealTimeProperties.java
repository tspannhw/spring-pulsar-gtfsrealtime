package dev.datainmotion.gtfsrealtime.service;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * api:
 *   mtaalerts-url: http://gtfsrt.prod.obanyc.com/alerts?key=
 *   vehiclepositions-url: http://gtfsrt.prod.obanyc.com/vehiclePositions?key=
 *   tripupdates-url: http://gtfsrt.prod.obanyc.com/tripUpdates?key=
 *   key: ${GTFS_KEY:}
 */
@ConfigurationProperties("api")
public class GTFSRealTimeProperties {

	private final String mtaalertsUrl;
	private final String vehiclepositionsUrl;
	private final String tripupdatesUrl;
	private final String key;

	@ConstructorBinding
	public GTFSRealTimeProperties(
			@DefaultValue("http://gtfsrt.prod.obanyc.com/alerts?key=") String alertsUrl,
			@DefaultValue("http://gtfsrt.prod.obanyc.com/vehiclePositions?key=")String vpUrl,
			@DefaultValue("http://gtfsrt.prod.obanyc.com/tripUpdates?key=")String trUrl,
			String key) {
		this.mtaalertsUrl = alertsUrl;
		this.vehiclepositionsUrl = vpUrl;
		this.tripupdatesUrl = trUrl;
		this.key = key;
	}

	public String getMtaalertsUrl() {
		return mtaalertsUrl;
	}

	public String getVehiclepositionsUrl() {
		return vehiclepositionsUrl;
	}

	public String getTripupdatesUrl() { return tripupdatesUrl; }

	public String getKey() {
		return key;
	}
}