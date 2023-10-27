package com.moviebookingapp.service;

import com.moviebookingapp.dto.BookedTableDto;
import com.moviebookingapp.excpetion.TicketException;
import com.moviebookingapp.model.Ticket;

import java.util.List;

public interface TicketService {

    public Ticket bookTicket(BookedTableDto bookedTableDto,String movieId) throws TicketException;
    public List<Ticket> getTicketTable();

}
