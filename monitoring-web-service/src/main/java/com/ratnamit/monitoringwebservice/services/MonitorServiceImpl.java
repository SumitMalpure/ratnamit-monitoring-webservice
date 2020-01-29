package com.ratnamit.monitoringwebservice.services;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ratnamit.monitoringwebservice.config.MonitorConfig;
import com.ratnamit.monitoringwebservice.dto.MonitorEntity;
import com.ratnamit.monitoringwebservice.dto.MonitorRecord;
import com.ratnamit.monitoringwebservice.dto.MonitorRequestEntity;
import com.ratnamit.monitoringwebservice.services.interfaces.MonitorService;
/**
 * @author sumitmalpure1089
 *
 */
@Service
public class MonitorServiceImpl implements MonitorService {

	private MonitorConfig config;
	private Logger logger;
	private Map<String, MonitorEntity> servicesMap;
	private Map<String, ArrayDeque<MonitorRecord>> monitorMap;

	public MonitorServiceImpl(MonitorConfig config) {
		logger = LoggerFactory.getLogger(MonitorServiceImpl.class);
		this.config = config;
		servicesMap = new ConcurrentHashMap<>();
		monitorMap = new ConcurrentHashMap<>();
	}

	@Override
	public void registerMicroservices(List<MonitorRequestEntity> entities) {
		for (MonitorRequestEntity entity : entities) {
			MonitorEntity monitorEntity = new MonitorEntity();
			monitorEntity.setServiceName(entity.getServiceName());
			monitorEntity.setServiceURL(entity.getServiceURL());
			monitorEntity.setActive(true);
			servicesMap.put(entity.getServiceName(), monitorEntity);

			if (!monitorMap.containsKey(entity.getServiceName())) {
				monitorMap.put(entity.getServiceName(), new ArrayDeque<MonitorRecord>());
			}
		}
	}

	@Override
	public void deregisterMicroservices(List<MonitorRequestEntity> entities) {
		for (MonitorRequestEntity entity : entities) {
			servicesMap.remove(entity.getServiceName());
			monitorMap.remove(entity.getServiceName());
		}
	}

	@Override
	public List<MonitorEntity> getAllMicroservices() {
		return servicesMap.values().stream().collect(Collectors.toList());
	}

	@Override
	public Long getTotalDownTimeInMS(String msName) {

		if (monitorMap.containsKey(msName)) {
			ArrayDeque<MonitorRecord> log = monitorMap.get(msName).clone();

			if (log.isEmpty()) {
				return 0L;
			} else {
				Iterator<MonitorRecord> i = log.iterator();

				MonitorRecord previous = i.next();
				Long sum = 0L;
				while (i.hasNext()) {
					MonitorRecord current = i.next();

					if (!previous.getActive()) {
						sum += current.getTimeStamp() - previous.getTimeStamp();
					}

					previous = current;
				}

				if (!previous.getActive()) {
					sum += new Date().getTime() - previous.getTimeStamp();
				}

				return sum > config.getMonitoringDurationMs() ? config.getMonitoringDurationMs() : sum;
			}

		} else {
			return -1L;
		}

	}

	@Override
	public Long getLastMinuteDownTimeInMS(String msName, Long currentTimestamp) {

		if (monitorMap.containsKey(msName)) {
			ArrayDeque<MonitorRecord> log = monitorMap.get(msName).clone();

			if (log.isEmpty()) {
				return 0L;
			} else {
				Iterator<MonitorRecord> i = log.iterator();

				MonitorRecord previous = i.next();
				Long sum = 0L;
				Long lastMinuteTimestamp = currentTimestamp - 60000L;

				while (i.hasNext()) {
					MonitorRecord current = i.next();
					if (!previous.getActive() && previous.getTimeStamp() > lastMinuteTimestamp) {
						sum += current.getTimeStamp() - previous.getTimeStamp();
					}

					previous = current;
				}

				return sum;
			}

		} else {
			return -1L;
		}

	}

	@Override
	public void logHealthCheck(MonitorRecord record) {
		if (monitorMap.containsKey(record.getMsName())) {
			ArrayDeque<MonitorRecord> dq = monitorMap.get(record.getMsName());

			if (!dq.isEmpty()) {
				while (record.getTimeStamp() - dq.getFirst().getTimeStamp() > config.getMonitoringDurationMs()) {
					dq.removeFirst();
				}

				dq.add(record);
			} else {
				dq.add(record);

			}

			logger.info("Deque size: {}", dq.size());
		}
	}

	@Override
	public MonitorRecord healthCheck(MonitorEntity entity) {
		RestTemplate restTemplate = new RestTemplate();
		MonitorRecord record = new MonitorRecord();
		record.setMsName(entity.getServiceName());

		StringBuilder url = new StringBuilder(entity.getServiceURL());
		url.append(config.getHealthEndpoint());

		try {
			ResponseEntity<String> result = restTemplate.getForEntity(url.toString(), String.class);

			if (HttpStatus.OK == result.getStatusCode()) {
				record.setActive(Boolean.TRUE);
				servicesMap.get(entity.getServiceName()).setActive(Boolean.TRUE);
			} else {
				record.setActive(Boolean.FALSE);
				servicesMap.get(entity.getServiceName()).setActive(Boolean.FALSE);
			}
		} catch (Exception e) {
			record.setActive(Boolean.FALSE);
			servicesMap.get(entity.getServiceName()).setActive(Boolean.FALSE);
		}

		record.setTimeStamp(new Date().getTime());

		return record;
	}

	@Override
	public void resetUnavailabilityStats(String msName) {
		if (monitorMap.containsKey(msName)) {
			ArrayDeque<MonitorRecord> dq = monitorMap.get(msName);
			monitorMap.remove(msName);
			monitorMap.put(msName, new ArrayDeque<MonitorRecord>());
		}
	}

}
