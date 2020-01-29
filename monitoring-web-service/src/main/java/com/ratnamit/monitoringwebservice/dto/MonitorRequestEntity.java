package com.ratnamit.monitoringwebservice.dto;
/**
 * @author sumitmalpure1089
 *
 */
public class MonitorRequestEntity {

	private String serviceName;
	private String serviceURL;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

}
