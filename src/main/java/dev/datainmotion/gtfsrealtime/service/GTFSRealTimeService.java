package dev.datainmotion.gtfsrealtime.service;

import com.google.protobuf.ExtensionRegistry;
import com.google.transit.realtime.GtfsRealtime;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import dev.datainmotion.gtfsrealtime.model.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	 *
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

	public List<Alert> getMTAVehiclePositions() {
		ExtensionRegistry registry = ExtensionRegistry.newInstance();
		URL url = null;
		try {
			url = new URL(gtfsRealTimeProperties.getVehiclepositionsUrl() + gtfsRealTimeProperties.getKey());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			log.error("Failed to retrieve vehicles gtfsrealtimedue to: " + e.getMessage(), e);
			return Collections.emptyList();
		}

		List<Alert> alerts = new ArrayList<>();
		FeedMessage feed = null;
		try {
			feed = FeedMessage.parseFrom(url.openStream());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Failed to retrieve gtfsrealtimedue to: " + e.getMessage(), e);
			return Collections.emptyList();
		}

		if ( feed != null && feed.getSerializedSize() > 0 ) {

			for (FeedEntity entity : feed.getEntityList()) {
				System.out.println("Feed Entity ID:" + entity.getId());
				if (entity.hasVehicle() && entity.getVehicle() !=null) {
					System.out.println("Vehicletostring:" + entity.getVehicle().getVehicle().toString());
					GtfsRealtime.VehiclePosition vehicle = entity.getVehicle();
					System.out.println("Vehicleid:" +
							vehicle.getVehicle().getId() + ",label=" +
							vehicle.getVehicle().getLabel() + ",plate=" +
							vehicle.getVehicle().getLicensePlate() );
					if ( vehicle.getTrip() != null ) {
						GtfsRealtime.TripDescriptor trip = vehicle.getTrip();
						if ( trip.hasStartTime()) {
							System.out.println("VT st: " +trip.getStartTime());
						}
						if ( trip.hasRouteId() ) {
							System.out.println("RID: " +trip.getRouteId());
						}
						if ( trip.hasDirectionId() ) {
							System.out.println("DID: " +trip.getDirectionId());
						}
						if ( trip.hasStartDate()) {
							System.out.println("STDt:" + trip.getStartDate());
						}
						if ( trip.hasTripId()) {
							System.out.println("trid: " +trip.getTripId());
						}
					}
				}

				if (entity.hasAlert()) {
					System.out.println("Alert:" + entity.getAlert());
					//entity.getAlert().getEffect()
					// alert iterate
					GtfsRealtime.Alert alert = entity.getAlert();
					System.out.println("alert: " + alert.getHeaderText().toString());
				}

				if (entity.hasTripUpdate()) {
					System.out.println(entity.getTripUpdate());
					GtfsRealtime.TripUpdate tripupdate = entity.getTripUpdate();
					System.out.println("Trip update: " +
					tripupdate.getDelay() + "," +
					tripupdate.getVehicle() + "," +
							tripupdate.getSerializedSize() + "," +
							tripupdate.getTimestamp());
				}
			}
		}

		return alerts;
	}

	/**
	 *
	 */
	public void getMTATripUpdates() {
		URL url = null;
		try {
			url = new URL ( gtfsRealTimeProperties.getTripupdatesUrl() +  gtfsRealTimeProperties.getKey() );
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

			if (entity.hasTripUpdate()) {
				GtfsRealtime.TripUpdate tripUpdate = entity.getTripUpdate();

				//System.out.println("Trip Update: " + tripUpdate.toString());

				GtfsRealtime.TripDescriptor trip = tripUpdate.getTrip();

				System.out.println("Trip:" +
						trip.getTripId() + "," +
						trip.getStartDate() + "," +
						trip.getScheduleRelationship().getValueDescriptor().getFullName() + "," +
						trip.getRouteId() + "," +
						trip.getDirectionId() );

				if ( trip.hasScheduleRelationship() )
				{
					GtfsRealtime.TripDescriptor.ScheduleRelationship sr = trip.getScheduleRelationship();

					System.out.println("sr:" + sr.getNumber() + "," +
					sr.getValueDescriptor().getFullName() + "," +
							sr.getDescriptorForType().getFullName() );
				}

				if ( trip.hasStartDate()) {
					//trip.getStartDate()
				}

				if ( tripUpdate.hasDelay()) {

					System.out.println("Delay: " + tripUpdate.getDelay() );

//					GtfsRealtime.TripUpdate.StopTimeUpdate  stopTimeUpdate = tripUpdate.getStopTimeUpdate(0);
//
//					System.out.println("Stop Time Update:" + stopTimeUpdate.toString());
				}


				if ( tripUpdate.hasTimestamp() ) {
					System.out.println("has timestamp");
				}

//
//				for update in en["trip_update"]["stop_time_update"]:
//				if 'departure' in update:
//				departure = update["departure"]["time"]
//				stp_id = update["stop_id"]
//								#if stp_id in result[line_id][vehicleId]:
//								#	result[line_id][vehicleId][stp_id].append(epoch_to_realtime(departure))
//								#else:
//				result[line_id][vehicleId][stp_id] = epoch_to_realtime(departure)


				System.out.println("Trip Update fields: " +
						tripUpdate.getDelay() + "," +
						tripUpdate.getVehicle() + "," +
						tripUpdate.getSerializedSize() + "," +
						tripUpdate.getTimestamp());
			}
			if ( entity.hasAlert() ) {
				System.out.println("Alert:" + entity.getAlert());
			}
		}
	}

	/**
	*/
	// 
//	public static void main(String args[]) {
//		//System.out.println("alerts");
//		//getMTAAlerts();
//		//System.out.println("trip updates");
//		getMTATripUpdates();
//		//System.out.println("veh positions");
//		//getMTAVehiclePositions();
//	}
}