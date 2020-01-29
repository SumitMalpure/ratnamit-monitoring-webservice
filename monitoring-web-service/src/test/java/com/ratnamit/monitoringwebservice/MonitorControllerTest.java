/**
 * 
 */
package com.ratnamit.monitoringwebservice;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.ratnamit.monitoringwebservice.controllers.MonitorController;
import com.ratnamit.monitoringwebservice.domain.MonitorRequest;
import com.ratnamit.monitoringwebservice.dto.MonitorRequestEntity;
import com.ratnamit.monitoringwebservice.services.interfaces.MonitorService;

/**
 * @author sumitmalpure1089
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MonitorControllerTest {

	@Autowired
	MonitorController testController;
	
	@MockBean
	MonitorService monitorServiceMock;
	
	@Test
	public void registerSuccess() 
	{
		MonitorRequest request = new  MonitorRequest();
		
		MonitorRequestEntity requestEntity = new MonitorRequestEntity();
		requestEntity.setServiceName("employee");
		requestEntity.setServiceURL("http://localhost:8084");
		
		List<MonitorRequestEntity> serviceEntities= new ArrayList<MonitorRequestEntity>();
		serviceEntities.add(requestEntity);
		
		request.setMsEntityList(serviceEntities);
		
		Mockito.doNothing().when(monitorServiceMock).registerMicroservices(ArgumentMatchers.any(List.class));
		ResponseEntity<String> response = testController.registerMicroservice(request);
		
		assertEquals(HttpStatus.OK,response.getStatusCode());
		Mockito.verify(monitorServiceMock, times(1)).registerMicroservices(ArgumentMatchers.any(List.class));
	}
	
	@Test
	public void deregisterSuccess() 
	{
		MonitorRequest request = new  MonitorRequest();
		
		MonitorRequestEntity requestEntity = new MonitorRequestEntity();
		requestEntity.setServiceName("employee");
		requestEntity.setServiceURL("http://localhost:8084");
		
		List<MonitorRequestEntity> serviceEntities= new ArrayList<MonitorRequestEntity>();
		serviceEntities.add(requestEntity);
		
		request.setMsEntityList(serviceEntities);
		
		
		Mockito.doNothing().when(monitorServiceMock).deregisterMicroservices(ArgumentMatchers.any(List.class));
		Map<String, Boolean>response = testController.deregisterMicroservice(request);
		
		assertEquals(Boolean.TRUE,response.get("deregistered"));
		Mockito.verify(monitorServiceMock, times(1)).deregisterMicroservices(ArgumentMatchers.any(List.class));
	}

}
