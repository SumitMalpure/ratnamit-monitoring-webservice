/**
 * 
 */
package com.ratnamit.monitoringwebservice.domain;

import java.util.List;

import com.ratnamit.monitoringwebservice.dto.MonitorRequestEntity;

import lombok.Data;

/**
 * @author sumitmalpure1089
 *
 */
@Data
public class MonitorRequest {

	List<MonitorRequestEntity> msEntityList;

	public List<MonitorRequestEntity> getMsEntityList() {
		return msEntityList;
	}

	public void setMsEntityList(List<MonitorRequestEntity> msEntityList) {
		this.msEntityList = msEntityList;
	}
}
