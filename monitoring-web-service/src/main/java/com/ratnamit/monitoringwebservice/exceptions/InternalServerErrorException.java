package com.ratnamit.monitoringwebservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * @author sumitmalpure1089
 *
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
@ResponseBody
public class InternalServerErrorException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public InternalServerErrorException(String errorMessage) 
	{
		super("Internal Server Error - " + errorMessage);
	}
}
