package com.moviebookingapp.excpetion;

public class TicketException extends Exception{
    private static final long serialVersionUID = 1L;

    public TicketException(String exceptionMessage) {
        super(exceptionMessage);
    }

}
