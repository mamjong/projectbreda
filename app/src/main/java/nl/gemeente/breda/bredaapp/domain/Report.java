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
	private String address;
	private double latitude;
	private double longitude;
	private boolean isFavorite;
	private int upvotes;
	
	public Report() {
	}
	
	public boolean isFavorite() {
		return isFavorite;
	}
	
	public void setFavorite(boolean favorite) {
		isFavorite = favorite;
	}
	
	public int getUpvotes() {
		return upvotes;
	}
	
	public void setUpvotes(int upvotes) {
		this.upvotes = upvotes;
	}
	
	public String getServiceRequestId() {
		return serviceRequestId;
	}
	
	public void setServiceRequestId(String serviceRequestId) {
		this.serviceRequestId = serviceRequestId;
	}
	
	public String getServiceCode() {
		return serviceCode;
	}
	
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getRequestedDatetime() {
		return requestedDatetime;
	}
	
	public void setRequestedDatetime(String requestedDatetime) {
		this.requestedDatetime = requestedDatetime;
	}
	
	public String getUpdatedDatetime() {
		return updatedDatetime;
	}
	
	public void setUpdatedDatetime(String updatedDatetime) {
		this.updatedDatetime = updatedDatetime;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatusNotes() {
		return statusNotes;
	}
	
	public void setStatusNotes(String statusNotes) {
		this.statusNotes = statusNotes;
	}
	
	public String getAgencyResponsible() {
		return agencyResponsible;
	}
	
	public void setAgencyResponsible(String agencyResponsible) {
		this.agencyResponsible = agencyResponsible;
	}
	
	public String getServiceName() {
		return serviceName;
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public String getMediaUrl() {
		return mediaUrl;
	}
	
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
}
