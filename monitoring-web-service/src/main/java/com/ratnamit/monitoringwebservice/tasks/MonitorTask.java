package com.ratnamit.monitoringwebservice.tasks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ratnamit.monitoringwebservice.dto.MonitorEntity;
import com.ratnamit.monitoringwebservice.dto.MonitorRecord;
import com.ratnamit.monitoringwebservice.services.interfaces.MonitorService;
/**
 * @author sumitmalpure1089
 *
 */
@Component
public class MonitorTask
{

	@Autowired
	MonitorService service;
	
	Logger logger = LoggerFactory.getLogger(MonitorTask.class);
	
	@Scheduled(fixedRateString = "${monitoring.callIntervalInSec:20}000")
	public void scheduleTaskWithFixedRate() 
	{
		System.out.println("Calling monitor:"+new Date().getTime());
		monitor();
	}

	

	private void monitor()
	{
		
		List<MonitorEntity> microservices = service.getAllMicroservices();
		if (microservices != null && !microservices.isEmpty())
		{
			ExecutorService executor = Executors.newFixedThreadPool(microservices.size());
			
			List<FutureTask<MonitorRecord>> allTasks = new ArrayList<>();
			
			for (MonitorEntity microservice : microservices)
			{
				Callable<MonitorRecord> callable = () -> {
					return service.healthCheck(microservice);
				};
				
				FutureTask<MonitorRecord> futureTask = new FutureTask<>(callable);
				allTasks.add(futureTask);
				executor.execute(futureTask);
			}
			
			
			for (FutureTask<MonitorRecord> task : allTasks)
			{
				try
				{
					MonitorRecord mresult =  task.get();
					
					service.logHealthCheck(mresult);
					
				}
				catch(Exception e)
				{
					logger.error("Execution failed with " + e);
				}
			}
			
			 executor.shutdown();
		}
		
	}
	
}
