package com.ratnamit.monitoringwebservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * @author sumitmalpure1089
 *
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@ResponseBody
public class BadRequestException extends RuntimeException
{

	private static final long serialVersionUID = 1L;

	
	public BadRequestException(String errorMessage) 
	{
		super("Bad Request - " + errorMessage);
	}
	
}
