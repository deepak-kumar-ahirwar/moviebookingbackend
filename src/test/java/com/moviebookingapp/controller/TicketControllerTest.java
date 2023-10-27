package com.moviebookingapp.controller;

import com.moviebookingapp.dto.BookedTableDto;
import com.moviebookingapp.excpetion.TicketException;
import com.moviebookingapp.model.Ticket;
import com.moviebookingapp.service.TicketService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TicketControllerTest {

    @InjectMocks
    private TicketController ticketController;

    @Mock
    private TicketService ticketService;



    @Test
    public void testBookTicket() throws TicketException {
        String movieId = "1";
        BookedTableDto bookedTableDto = new BookedTableDto();
        Ticket bookedTicket = new Ticket();

        when(ticketService.bookTicket(eq(bookedTableDto), eq(movieId))).thenReturn(bookedTicket);

        ResponseEntity<Ticket> response = ticketController.bookTicket(bookedTableDto, movieId);

        verify(ticketService, times(1)).bookTicket(eq(bookedTableDto), eq(movieId));
        assert(response.getStatusCode() == HttpStatus.OK);
        assert(response.getBody() != null);
        assert(response.getBody() == bookedTicket);
    }

    @Test
    public void testBookedTableShow() {
        List<Ticket> bookedTable = new ArrayList<>();
        bookedTable.add(new Ticket());

        when(ticketService.getTicketTable()).thenReturn(bookedTable);

        ResponseEntity<List<Ticket>> response = ticketController.bookedTableShow();

        verify(ticketService, times(1)).getTicketTable();
        assert(response.getStatusCode() == HttpStatus.OK);
        assert(response.getBody() != null);
        assert(response.getBody().size() == 1); // Check if one ticket is returned
    }
}
