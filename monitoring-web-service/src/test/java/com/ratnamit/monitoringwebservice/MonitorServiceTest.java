package com.ratnamit.monitoringwebservice;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ratnamit.monitoringwebservice.config.MonitorConfig;
import com.ratnamit.monitoringwebservice.dto.MonitorRecord;
import com.ratnamit.monitoringwebservice.dto.MonitorRequestEntity;
import com.ratnamit.monitoringwebservice.services.interfaces.MonitorService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MonitorServiceTest {
	@Autowired
	MonitorService monitorService;

	@Autowired
	MonitorConfig config;

	@Test
	public void testDownTimeOnServiceRegistration() {
		List<MonitorRequestEntity> serviceEntities = new ArrayList<MonitorRequestEntity>();

		for (int i = 1; i <= 3; i++) {

			MonitorRequestEntity requestEntity = new MonitorRequestEntity();
			requestEntity.setServiceName("microservice" + i);
			requestEntity.setServiceURL("url" + i);
			serviceEntities.add(requestEntity);

		}

		monitorService.registerMicroservices(serviceEntities);

		assertEquals(Long.valueOf(0L), monitorService.getTotalDownTimeInMS("microservice1"));
		assertEquals(Long.valueOf(0L), monitorService.getTotalDownTimeInMS("microservice2"));
		assertEquals(Long.valueOf(0L), monitorService.getTotalDownTimeInMS("microservice3"));
	}

	@Test
	public void testDownTimeAfterUnregisteringMS() {
		List<MonitorRequestEntity> serviceEntities = new ArrayList<MonitorRequestEntity>();

		for (int i = 1; i <= 3; i++) {

			MonitorRequestEntity requestEntity = new MonitorRequestEntity();
			requestEntity.setServiceName("microservice" + i);
			requestEntity.setServiceURL("url" + i);
			serviceEntities.add(requestEntity);

		}

		monitorService.registerMicroservices(serviceEntities);

		assertEquals(Long.valueOf(0L), monitorService.getTotalDownTimeInMS("microservice1"));
		assertEquals(Long.valueOf(0L), monitorService.getTotalDownTimeInMS("microservice2"));
		assertEquals(Long.valueOf(0L), monitorService.getTotalDownTimeInMS("microservice3"));

		monitorService.deregisterMicroservices(serviceEntities);

		assertEquals(Long.valueOf(-1L), monitorService.getTotalDownTimeInMS("microservice1"));
		assertEquals(Long.valueOf(-1L), monitorService.getTotalDownTimeInMS("microservice2"));
		assertEquals(Long.valueOf(-1L), monitorService.getTotalDownTimeInMS("microservice3"));
	}

	@Test
	public void testTotalDownTimeForService() {

		List<MonitorRequestEntity> serviceEntities = new ArrayList<MonitorRequestEntity>();

		MonitorRequestEntity requestEntity = new MonitorRequestEntity();
		requestEntity.setServiceName("employeeservice");
		requestEntity.setServiceURL("url");

		serviceEntities.add(requestEntity);
		monitorService.registerMicroservices(serviceEntities);

		MonitorRecord record = new MonitorRecord();
		record.setMsName("employeeservice");
		record.setActive(true);
		record.setTimeStamp(0L);
		monitorService.logHealthCheck(record);

		MonitorRecord record2 = new MonitorRecord();
		record2.setMsName("employeeservice");
		record2.setActive(false);
		record2.setTimeStamp(10000L);
		monitorService.logHealthCheck(record2);

		MonitorRecord record3 = new MonitorRecord();
		record3.setMsName("employeeservice");
		record3.setActive(true);
		record3.setTimeStamp(20000L);
		monitorService.logHealthCheck(record3);

		assertEquals(Long.valueOf(10000L), monitorService.getTotalDownTimeInMS("employeeservice"));
	}

}
