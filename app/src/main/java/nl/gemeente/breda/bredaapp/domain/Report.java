package nl.gemeente.breda.bredaapp.domain;

import java.io.Serializable;

public class Report implements Serializable {
    private String serviceRequestId;
    private String serviceCode;
    private String description;
    private String requestedDatetime;
    private String updatedDatetime;
    private String status;
    private String statusNotes;
    private String agencyResponsible;
    private String serviceName;
    private String mediaUrl;
    private double latitude;
    private double longitude;

    public Report() {}

    public void setServiceRequestId(String serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequestedDatetime(String requestedDatetime) {
        this.requestedDatetime = requestedDatetime;
    }

    public void setUpdatedDatetime(String updatedDatetime) {
        this.updatedDatetime = updatedDatetime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStatusNotes(String statusNotes) {
        this.statusNotes = statusNotes;
    }

    public void setAgencyResponsible(String agencyResponsible) {
        this.agencyResponsible = agencyResponsible;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getServiceRequestId() {
        return serviceRequestId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getDescription() {
        return description;
    }

    public String getRequestedDatetime() {
        return requestedDatetime;
    }

    public String getUpdatedDatetime() {
        return updatedDatetime;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusNotes() {
        return statusNotes;
    }

    public String getAgencyResponsible() {
        return agencyResponsible;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
