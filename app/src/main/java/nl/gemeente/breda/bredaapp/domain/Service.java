package nl.gemeente.breda.bredaapp.domain;

public class Service {
    private String serviceCode;
    private String serviceName;
    private String description;
    private boolean metadata;
    private String type;
    private String keywords;
    private String group;

    public Service() {
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMetadata(boolean metadata) {
        this.metadata = metadata;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isMetadata() {
        return metadata;
    }

    public String getType() {
        return type;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getGroup() {
        return group;
    }
	
	@Override
	public String toString() {
		return serviceName;
	}
}
