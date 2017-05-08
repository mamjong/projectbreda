package nl.gemeente.breda.bredaapp.Domain;

public class Report {

    private int reportID;
    private String reportCategory, reportType, reportImageUrl;

    public Report() {
    }

    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public String getReportCategory() {
        return reportCategory;
    }

    public void setReportCategory(String reportCategory) {
        this.reportCategory = reportCategory;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportImageUrl() {
        return reportImageUrl;
    }

    public void setReportImageUrl(String reportImageUrl) {
        this.reportImageUrl = reportImageUrl;
    }
}
