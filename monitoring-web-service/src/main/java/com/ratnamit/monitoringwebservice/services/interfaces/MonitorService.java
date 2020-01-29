package com.ratnamit.monitoringwebservice.services.interfaces;

import java.util.List;

import com.ratnamit.monitoringwebservice.dto.MonitorEntity;
import com.ratnamit.monitoringwebservice.dto.MonitorRecord;
import com.ratnamit.monitoringwebservice.dto.MonitorRequestEntity;
/**
 * @author sumitmalpure1089
 *
 */
public interface MonitorService
{
	public void registerMicroservices(List<MonitorRequestEntity> entities);
	public void deregisterMicroservices(List<MonitorRequestEntity> entities);
	public List<MonitorEntity> getAllMicroservices();
	public Long getTotalDownTimeInMS(String msName);
	public Long getLastMinuteDownTimeInMS(String msName,Long currentTimestamp);
	public void logHealthCheck(MonitorRecord record);
	public MonitorRecord healthCheck(MonitorEntity entity);
	public void resetUnavailabilityStats(String msName);
	


}
