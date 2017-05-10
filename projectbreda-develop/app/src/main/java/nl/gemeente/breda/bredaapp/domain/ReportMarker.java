//================================================================================
// This class is made by:
// - Thimo Koolen
//================================================================================

package nl.gemeente.breda.bredaapp.domain;

public class ReportMarker {
	
	//================================================================================
	// Properties
	//================================================================================
	
	private double latitude;
	private double longitude;
	private String title;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	public ReportMarker(double latitude, double longitude, String title) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.title = title;
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public String getTitle() {
		return title;
	}
	
	//================================================================================
	// Mutators
	//================================================================================
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
}
