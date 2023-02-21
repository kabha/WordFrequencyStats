package com.cylo.controllers.advice;



import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cylo.exceptions.ErrorWhileProcessingRequest;
import com.cylo.response.ErrorResponse;
import com.cylo.response.RequestStatus;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class CustomExceptionHandler {
	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CustomExceptionHandler.class);
	
	@ExceptionHandler
	@ResponseBody
	public ResponseEntity<Object> handleErrorWhileProcessingRequestException(HttpServletRequest request,
			ErrorWhileProcessingRequest exception) {
		LOG.error(exception.getMessage(), exception);
		ErrorResponse baseResponse = new ErrorResponse(exception.getMessage(), request.getRequestURI(),
				RequestStatus.ERROR);
		LOG.info("Returning: {}", baseResponse);
		
		if (exception.isBadRequest()) {
			return new ResponseEntity<Object>(baseResponse, HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<Object>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}