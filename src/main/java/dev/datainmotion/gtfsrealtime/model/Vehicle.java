package dev.datainmotion.gtfsrealtime.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.StringJoiner;

/**
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "feedEntityID",
        "activePeriodStart",
        "activePeriodEnd",
        "headerText",
        "headerLanguage",
        "agency",
        "tripRouteId",
        "tripDirectionId",
        "descriptionText",
        "descriptionLanguage",
        "effect",
        "stopID",
        "tripStartDate",
        "tripStartTime",
        "routeID",
        "directionID",
        "url",
        "severityLevel",
        "cause"
})
public class Vehicle {

    private String feedEntityID;
    private long activePeriodStart;
    private long activePeriodEnd;
    private String headerText;
    private String headerLanguage;
    private String agency;
    private String tripRouteId;
    private long tripDirectionId;
    private String descriptionText;
    private String descriptionLanguage;
    private String effect;
    private String stopId;
    private String tripStartDate;
    private String tripStartTime;
    private String routeId;
    private long directionId;
    private String url;
    private String severityLevel;
    private String cause;

    @Override
    public String toString() {
        return new StringJoiner(", ", Vehicle.class.getSimpleName() + "[", "]")
                .add("feedEntityID='" + feedEntityID + "'")
                .add("activePeriodStart=" + activePeriodStart)
                .add("activePeriodEnd=" + activePeriodEnd)
                .add("headerText='" + headerText + "'")
                .add("headerLanguage='" + headerLanguage + "'")
                .add("agency='" + agency + "'")
                .add("tripRouteId='" + tripRouteId + "'")
                .add("tripDirectionId=" + tripDirectionId)
                .add("descriptionText='" + descriptionText + "'")
                .add("descriptionLanguage='" + descriptionLanguage + "'")
                .add("effect='" + effect + "'")
                .add("stopId='" + stopId + "'")
                .add("tripStartDate='" + tripStartDate + "'")
                .add("tripStartTime='" + tripStartTime + "'")
                .add("routeId='" + routeId + "'")
                .add("directionId=" + directionId)
                .add("url='" + url + "'")
                .add("severityLevel='" + severityLevel + "'")
                .add("cause='" + cause + "'")
                .toString();
    }

    public Vehicle() {
        super();
    }

    public Vehicle(String feedEntityID, long activePeriodStart, long activePeriodEnd, String headerText, String headerLanguage, String agency, String tripRouteId, long tripDirectionId, String descriptionText, String descriptionLanguage, String effect, String stopId, String tripStartDate, String tripStartTime, String routeId, long directionId, String url, String severityLevel, String cause) {
        super();
        this.feedEntityID = feedEntityID;
        this.activePeriodStart = activePeriodStart;
        this.activePeriodEnd = activePeriodEnd;
        this.headerText = headerText;
        this.headerLanguage = headerLanguage;
        this.agency = agency;
        this.tripRouteId = tripRouteId;
        this.tripDirectionId = tripDirectionId;
        this.descriptionText = descriptionText;
        this.descriptionLanguage = descriptionLanguage;
        this.effect = effect;
        this.stopId = stopId;
        this.tripStartDate = tripStartDate;
        this.tripStartTime = tripStartTime;
        this.routeId = routeId;
        this.directionId = directionId;
        this.url = url;
        this.severityLevel = severityLevel;
        this.cause = cause;
    }

    public String getFeedEntityID() {
        return feedEntityID;
    }

    public void setFeedEntityID(String feedEntityID) {
        this.feedEntityID = feedEntityID;
    }

    public long getActivePeriodStart() {
        return activePeriodStart;
    }

    public void setActivePeriodStart(long activePeriodStart) {
        this.activePeriodStart = activePeriodStart;
    }

    public long getActivePeriodEnd() {
        return activePeriodEnd;
    }

    public void setActivePeriodEnd(long activePeriodEnd) {
        this.activePeriodEnd = activePeriodEnd;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public String getHeaderLanguage() {
        return headerLanguage;
    }

    public void setHeaderLanguage(String headerLanguage) {
        this.headerLanguage = headerLanguage;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getTripRouteId() {
        return tripRouteId;
    }

    public void setTripRouteId(String tripRouteId) {
        this.tripRouteId = tripRouteId;
    }

    public long getTripDirectionId() {
        return tripDirectionId;
    }

    public void setTripDirectionId(long tripDirectionId) {
        this.tripDirectionId = tripDirectionId;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public String getDescriptionLanguage() {
        return descriptionLanguage;
    }

    public void setDescriptionLanguage(String descriptionLanguage) {
        this.descriptionLanguage = descriptionLanguage;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }



    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getStopId() {
        return stopId;
    }

    public void setTripStartDate(String tripStartDate) {
        this.tripStartDate = tripStartDate;
    }

    public String getTripStartDate() {
        return tripStartDate;
    }

    public void setTripStartTime(String tripStartTime) {
        this.tripStartTime = tripStartTime;
    }

    public String getTripStartTime() {
        return tripStartTime;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setDirectionId(long directionId) {
        this.directionId = directionId;
    }

    public long getDirectionId() {
        return directionId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setSeverityLevel(String severityLevel) {
        this.severityLevel = severityLevel;
    }

    public String getSeverityLevel() {
        return severityLevel;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getCause() {
        return cause;
    }
}