/**
 * 
 */
package com.ratnamit.monitoringwebservice.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ratnamit.monitoringwebservice.domain.MonitorRequest;
import com.ratnamit.monitoringwebservice.domain.UnavailabilityResponse;
import com.ratnamit.monitoringwebservice.exceptions.InternalServerErrorException;
import com.ratnamit.monitoringwebservice.exceptions.ResourceNotFoundException;
import com.ratnamit.monitoringwebservice.services.interfaces.MonitorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author sumitmalpure1089
 *
 */
@RestController
@RequestMapping("/moniotring/v1")
@Api(value = "Microservice Monitoring System", description = "Operations pertaining to Microservice Monitoring Management System")
public class MonitorController {

	@Autowired
	MonitorService monitorService;

	@ApiOperation(value = "View a list of registered microservices", response = Map.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved the list"),
			@ApiResponse(code = 404, message = "Resource not found") })
	@RequestMapping(value = "/services", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Map<String, Object>> getRegisteredMicroservices() {
		return prepResponse(monitorService.getAllMicroservices());
	}

	@ApiOperation(value = "Reset unavailability stats of the microservice")
	@PutMapping("/services/{msName}/resetDownTimeStats")
	public ResponseEntity<String> resetServiceStats(
			@ApiParam(value = "Service Name to update service object", required = true) @PathVariable(value = "msName") String msName)
			throws ResourceNotFoundException {
		monitorService.resetUnavailabilityStats(msName);
		return ResponseEntity.status(HttpStatus.OK).body("OK");
	}

	@ApiOperation(value = "Registers the microservice for monitoring", response = Map.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully registered")})
	@RequestMapping(value = "/services", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> registerMicroservice(@RequestBody MonitorRequest request) {
		if (request != null && !request.getMsEntityList().isEmpty()) {
			monitorService.registerMicroservices(request.getMsEntityList());
		}
		return ResponseEntity.status(HttpStatus.OK).body("OK");
	}

	@ApiOperation(value = "Deregisters the microservice from monitoring", response = Map.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully Deregistered"),
			@ApiResponse(code = 404, message = "Resource not found") })
	@DeleteMapping(value = "/services/{id}")
	public Map<String, Boolean> deregisterMicroservice(@RequestBody MonitorRequest request) {
		monitorService.deregisterMicroservices(request.getMsEntityList());
		Map<String, Boolean> response = new HashMap<>();
		response.put("deregistered", Boolean.TRUE);
		return response;

	}

	@ApiOperation(value = "View unavailability stats of the selected microservice")
	@GetMapping("/services/{msName}/checkDownTimeStats")
	public ResponseEntity<Map<String, UnavailabilityResponse>> getUnavailabilityStats(
			@ApiParam(value = "Name of microservice for which unavailability stats to be retrieved", required = true) @PathVariable(value = "msName") String msName)
			throws ResourceNotFoundException {
		Long totalUnavailableTime = 0L;
		Long lastMinuteUnavailableTime = 0L;
		try {
			totalUnavailableTime = monitorService.getTotalDownTimeInMS(msName);
			lastMinuteUnavailableTime = monitorService.getLastMinuteDownTimeInMS(msName, new Date().getTime());
		} catch (Exception e) {
			StringBuilder errorMessage = new StringBuilder("Failure to get downtime of ms ");
			errorMessage.append(msName).append(" due to internal server error");

			throw new InternalServerErrorException(errorMessage.toString());
		}

		UnavailabilityResponse totalResponse = new UnavailabilityResponse();
		totalResponse.setMs(String.valueOf(totalUnavailableTime));
		totalResponse.setSecond(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(totalUnavailableTime)));
		totalResponse.setMinute(String.valueOf(TimeUnit.MILLISECONDS.toMinutes(totalUnavailableTime)));

		UnavailabilityResponse lastMinuteResponse = new UnavailabilityResponse();
		lastMinuteResponse.setMs(String.valueOf(lastMinuteUnavailableTime));
		lastMinuteResponse.setSecond(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(lastMinuteUnavailableTime)));
		lastMinuteResponse.setMinute(String.valueOf(TimeUnit.MILLISECONDS.toMinutes(lastMinuteUnavailableTime)));

		Map<String, UnavailabilityResponse> response = new HashMap<>();
		response.put("Total Unavailability stats for : "+msName, totalResponse);
		response.put("Last Minute Unavailability stats: "+msName, lastMinuteResponse);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private static ResponseEntity<Map<String, Object>> prepResponse(List<?> results) {
		Map<String, Object> dataMap = new HashMap<>();
		if (!Objects.isNull(results) && !results.isEmpty()) {
			dataMap.put("status", "success");
			dataMap.put("# of Results:", results.size());
			dataMap.put("result", results);
			return ResponseEntity.status(HttpStatus.OK).body(dataMap);
		} else {
			dataMap.put("# of Results:", 0);
			dataMap.put("result", "No results found for the given search criteria");
			return ResponseEntity.status(HttpStatus.OK).body(dataMap);
		}
	}

}
