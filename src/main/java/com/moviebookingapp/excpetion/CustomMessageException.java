package com.moviebookingapp.excpetion;

public class CustomMessageException extends RuntimeException {
	
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomMessageException(String exceptionMessage) {
	        super(exceptionMessage);
	    }
}
