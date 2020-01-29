/**
 * 
 */
package com.ratnamit.monitoringwebservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author sumitmalpure1089
 *
 */
@ConfigurationProperties("monitoring")
@Data
@Component
public class MonitorConfig
{
	private Long callIntervalMs = 20000L;
	private Long monitoringDurationMs = 3600000L; 
	private String healthEndpoint = "actuator/health";
	
	public Long getCallIntervalMs() {
		return callIntervalMs;
	}
	public void setCallIntervalMs(Long callIntervalMs) {
		this.callIntervalMs = callIntervalMs;
	}
	public Long getMonitoringDurationMs() {
		return monitoringDurationMs;
	}
	public void setMonitoringDurationMs(Long monitoringDurationMs) {
		this.monitoringDurationMs = monitoringDurationMs;
	}
	public String getHealthEndpoint() {
		return healthEndpoint;
	}
	public void setHealthEndpoint(String healthEndpoint) {
		this.healthEndpoint = healthEndpoint;
	}
}
