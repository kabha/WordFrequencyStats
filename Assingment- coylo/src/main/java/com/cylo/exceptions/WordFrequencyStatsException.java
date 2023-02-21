package com.cylo.exceptions;



/**
 * 
 * @author Daoud Kabha
 */


public class WordFrequencyStatsException extends Exception {
	
	/* serial */
	 private static final long serialVersionUID = 1L;
	 
	 /*attributes*/
	 protected String errorCode;

	 
	public WordFrequencyStatsException(String errorCode) {
		this.errorCode = errorCode;
	}


	public String getErrorCode() {
		return errorCode;
	}
}
