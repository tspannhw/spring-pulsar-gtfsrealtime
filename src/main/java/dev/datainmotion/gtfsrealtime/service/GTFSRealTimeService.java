package dev.datainmotion.gtfsrealtime.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.google.protobuf.ExtensionRegistry;
import dev.datainmotion.gtfsrealtime.model.Alert;
import dev.datainmotion.gtfsrealtime.model.Observation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URL;

import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import com.google.transit.realtime.*;
import com.google.transit.realtime.GtfsRealtime;
import com.google.protobuf.ExtensionRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 
 */
@Service
@EnableConfigurationProperties(GTFSRealTimeProperties.class)
public class GTFSRealTimeService {
	private static final Logger log = LoggerFactory.getLogger(GTFSRealTimeService.class);

	private final GTFSRealTimeProperties gtfsRealTimeProperties;

	/**
	 *
	 * @param props
	 */
	public GTFSRealTimeService(GTFSRealTimeProperties props) {
		super();
		this.gtfsRealTimeProperties = props;
	}

	/**
	 * get MTA Alerts
	 */
	public List<Alert> getMTAAlerts() {
		ExtensionRegistry registry = ExtensionRegistry.newInstance();
		URL url = null;
		try {
			url = new URL(gtfsRealTimeProperties.getMtaalertsUrl() + gtfsRealTimeProperties.getKey());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			log.error("Failed to retrieve gtfsrealtimedue to: " + e.getMessage(), e);
			return Collections.emptyList();
		}
		FeedMessage feed = null;
		try {
			feed = FeedMessage.parseFrom(url.openStream());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Failed to retrieve gtfsrealtimedue to: " + e.getMessage(), e);
			return Collections.emptyList();
		}
		List<Alert> alerts = new ArrayList<>();

		if ( feed != null && feed.getSerializedSize() > 0 ) {
			for (FeedEntity entity : feed.getEntityList()) {
				if (entity.hasAlert() && entity.getAlert() != null) {
					Alert alert = new Alert();
					alert.setFeedEntityID(entity.getId());
					GtfsRealtime.Alert rtAlert = entity.getAlert();

					if ( rtAlert.hasDescriptionText() && rtAlert.getDescriptionText() != null) {
						try {
							if ( rtAlert.getDescriptionText().getTranslationCount() > 0 ) {
								List<GtfsRealtime.TranslatedString.Translation> tlist = rtAlert.getDescriptionText().getTranslationList();

								if ( tlist != null ) {
									GtfsRealtime.TranslatedString.Translation translationText = tlist.get(0);
									if (translationText != null) {
										alert.setDescriptionText(translationText.getText());
										alert.setDescriptionLanguage(translationText.getLanguage());
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if ( rtAlert.hasCause() && rtAlert.getCause() != null) {
						alert.setCause( rtAlert.getCause().getValueDescriptor().toString() );
					}
					if ( rtAlert.hasHeaderText() && rtAlert.getHeaderText() != null) {
						try {
							if ( rtAlert.getHeaderText().getTranslationCount() > 0 ) {
								List<GtfsRealtime.TranslatedString.Translation> tlist = rtAlert.getHeaderText().getTranslationList();

								if ( tlist != null ) {
									GtfsRealtime.TranslatedString.Translation translationText = tlist.get(0);
									if (translationText != null) {
										alert.setHeaderText(translationText.getText());
										alert.setHeaderLanguage(translationText.getLanguage());
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if ( rtAlert.hasSeverityLevel() && rtAlert.getSeverityLevel() != null ) {
						alert.setSeverityLevel( rtAlert.getSeverityLevel().getValueDescriptor().getFullName());
					}
					if ( rtAlert.hasEffect() && rtAlert.getEffect() != null) {
						alert.setEffect ( rtAlert.getEffect().getValueDescriptor().getFullName());
					}
					if ( rtAlert.hasUrl() && rtAlert.getUrl() != null) {
						try {
							GtfsRealtime.TranslatedString urlX = rtAlert.getUrl();

							List<GtfsRealtime.TranslatedString.Translation> urlXList = urlX.getTranslationList();

							if ( urlXList != null && urlXList.size() > 0 ) {
								GtfsRealtime.TranslatedString.Translation stringUrl = urlXList.get(0);

								if ( stringUrl != null) {
									alert.setUrl(stringUrl.getText());
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if ( rtAlert.getActivePeriodCount() > 0 ) {
						try {
							List<GtfsRealtime.TimeRange> listOfPeriods = rtAlert.getActivePeriodList();

							if ( listOfPeriods != null ) {
								GtfsRealtime.TimeRange periodOfTime = listOfPeriods.get(0);

								if ( periodOfTime != null) {
									if (periodOfTime.hasStart()) {
										alert.setActivePeriodStart(periodOfTime.getStart());
									}
									if (periodOfTime.hasEnd()) {
										alert.setActivePeriodEnd(periodOfTime.getEnd());
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if ( rtAlert.getInformedEntityCount() > 0) {
						GtfsRealtime.EntitySelector informedEntity = rtAlert.getInformedEntity(0);
						alert.setAgency( informedEntity.getAgencyId() );

						if ( informedEntity.hasDirectionId()) {
							alert.setDirectionId( informedEntity.getDirectionId());
						}
						if ( informedEntity.hasRouteId()) {
							alert.setRouteId( informedEntity.getRouteId() );
						}
						if ( informedEntity.hasStopId()) {
							alert.setStopId( informedEntity.getStopId());
						}
						if ( informedEntity.hasTrip() &&  informedEntity.getTrip() != null ) {
							GtfsRealtime.TripDescriptor trip = informedEntity.getTrip();

							if ( trip.hasDirectionId()) {
								alert.setTripDirectionId(trip.getDirectionId());
							}
							if ( trip.hasRouteId() ) {
								alert.setTripRouteId(trip.getRouteId());
							}
							if ( trip.hasStartDate()) {
								alert.setTripStartDate(trip.getStartDate());
							}
							if ( trip.hasStartTime() ) {
								alert.setTripStartTime(trip.getStartTime());
							}
						}
					}
					
					alerts.add(alert);
				}
			}
		}

		return alerts;
	}

	public static void getMTAVehiclePositions() {
		URL url = null;
		try {
			url = new URL("http://gtfsrt.prod.obanyc.com/vehiclePositions?key=eb3b69d1-c869-4429-bee6-26a6cfc89cbe");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		FeedMessage feed = null;
		try {
			feed = FeedMessage.parseFrom(url.openStream());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		System.out.println("Size:" + feed.getSerializedSize());

		for (FeedEntity entity : feed.getEntityList()) {

			System.out.println("Feed Entity ID:" + entity.getId() );

			if ( entity.hasVehicle()) {
				System.out.println("Vehicle:" + entity.getVehicle().getVehicle().toString());
			}

			if ( entity.hasAlert() ) {
				System.out.println("Alert:" + entity.getAlert());

//				entity.getAlert().getDescriptionText()
			}

			if (entity.hasTripUpdate()) {
				System.out.println(entity.getTripUpdate());
			}
		}

	}

	public static void getMTATripUpdates() {
		URL url = null;
		try {
			url = new URL("http://gtfsrt.prod.obanyc.com/tripUpdates?key=b1f9a401-c839-417c-8995-d3a4a505b7ad");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		FeedMessage feed = null;
		try {
			feed = FeedMessage.parseFrom(url.openStream());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		System.out.println("Size:" + feed.getSerializedSize());

		for (FeedEntity entity : feed.getEntityList()) {

			System.out.println("Feed Entity ID:" + entity.getId() );

			if ( entity.hasAlert() ) {
				System.out.println("Alert:" + entity.getAlert());
			}
		}
	}

	/**
	 * https://github.com/OneBusAway/onebusaway-gtfs-realtime-api
	 * http://developer.onebusaway.org/modules/onebusaway-gtfs-realtime-api/current/
	 https://gtfs.org/resources/
	 https://github.com/cmoscardi/bus_kalman/blob/master/kalman.ipynb
	 https://github.com/laidig/gtfs-rt-printer/blob/master/src/main/java/net/transitdata/gtfsrt/Main.java
	 https://www.transit.land/feeds/
	 https://www.transit.land/routes/r-dr4u-607#sources
	 https://github.com/MobilityData/mobility-database-catalogs
	 http://bustime.mta.info/wiki/Developers/GTFSRt
	 https://github.com/FusionAuth/java-http
	 https://github.com/google/transit
	 https://github.com/google/transit/tree/master/gtfs-realtime/spec/en
	 https://github.com/google/transit/blob/master/gtfs-realtime/spec/en/service-alerts.md
	 https://github.com/google/transit/blob/master/gtfs-realtime/spec/en/trip-updates.md
	 https://github.com/google/transit/blob/master/gtfs-realtime/spec/en/vehicle-positions.md
	 https://gtfs.org/resources/data/


	 https://www.transit.land/feeds/f-bigbluebus~rt
	 https://www.transit.land/feeds/f-smartbus~rt
	 https://www.transit.land/feeds/f-nycferry~rt
	 https://www.transit.land/feeds/f-mta~nyc~rt~alerts
	 https://www.transit.land/feeds/f-mta~nyc~rt~subway~1~2~3~4~5~6~7
	 https://www.transit.land/feeds/f-mta~nyc~rt~subway~a~c~e
	 https://www.transit.land/feeds/f-mta~nyc~rt~subway~b~d~f~m
	 https://www.transit.land/feeds/f-mta~nyc~rt~subway~g
	 https://www.transit.land/feeds/f-mta~nyc~rt~subway~j~z
	 https://www.transit.land/feeds/f-mta~nyc~rt~subway~l
	 https://www.transit.land/feeds/f-mta~nyc~rt~subway~n~q~r~w
	 https://www.transit.land/feeds/f-mta~nyc~rt~subway~sir
	 https://www.transit.land/feeds/f-mtamaryland~marc~train~rt
	 https://www.transit.land/feeds/f-hazleton~public~transit~rt

	*/
	// https://github.com/google/transit/tree/master/gtfs-realtime/spec/en
//	public static void main(String args[]) {
//		//System.out.println("alerts");
//		//getMTAAlerts();
//		//System.out.println("trip updates");
//		getMTATripUpdates();
//		//System.out.println("veh positions");
//		//getMTAVehiclePositions();
//	}
}