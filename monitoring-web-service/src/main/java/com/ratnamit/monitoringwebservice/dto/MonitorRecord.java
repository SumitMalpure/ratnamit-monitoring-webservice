/**
 * 
 */
package com.ratnamit.monitoringwebservice.dto;

/**
 * @author sumitmalpure1089
 *
 */
public class MonitorRecord {

	private String msName;
	private Boolean active;
	private Long timeStamp;
	public String getMsName() {
		return msName;
	}
	public void setMsName(String msName) {
		this.msName = msName;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
}
