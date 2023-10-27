package com.moviebookingapp.serviceImpl;

import com.moviebookingapp.dto.BookedTableDto;
import com.moviebookingapp.excpetion.TicketException;
import com.moviebookingapp.kafka.KafkaProducer;
import com.moviebookingapp.model.Movie;
import com.moviebookingapp.model.Ticket;
import com.moviebookingapp.repository.MovieRepo;
import com.moviebookingapp.repository.TicketRepo;
import com.moviebookingapp.serviceimpl.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class TicketServiceImplTest {

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Mock
    private MovieRepo movieRepo;

    @Mock
    private TicketRepo ticketRepo;

  @Mock
  private KafkaProducer kafkaProducer;

    @Test
    public void testBookTicket_ValidBooking() throws TicketException {
        // Mock Movie
        Movie movie = new Movie();
        movie.setMovieId("1");
        movie.setMovieName("Movie 1");
        movie.setSeatAvailable(100);

        // Mock BookedTableDto
        BookedTableDto bookedTableDto = new BookedTableDto();
        bookedTableDto.setMovieName("Movie 1");
        bookedTableDto.setBookedSeats(2);
        bookedTableDto.setSeatNumber("A1,A2");

        // Mock Ticket
        Ticket ticket = new Ticket();
        ticket.setMovieName("Movie 1");
        ticket.setBookedSeats(2);
        ticket.setSeatNumber("A1,A2");

        when(movieRepo.findById("1")).thenReturn(Optional.of(movie));
        when(ticketRepo.save(any(Ticket.class))).thenReturn(ticket);
       doNothing().when(kafkaProducer).sendMessage("1");
        Ticket result = ticketService.bookTicket(bookedTableDto, "1");

        verify(movieRepo, times(1)).findById("1");
        verify(ticketRepo, times(1)).save(any(Ticket.class));

        assertEquals(98, movie.getSeatAvailable()); // Check if available seats were updated
        assertEquals("Movie 1", result.getMovieName());
        assertEquals(2, result.getBookedSeats());
        assertEquals("A1,A2", result.getSeatNumber());
    }

    @Test
    public void testBookTicket_SeatsNotAvailable() {
        // Mock Movie
        Movie movie = new Movie();
        movie.setMovieId("2");
        movie.setMovieName("Movie 2");
        movie.setSeatAvailable(1);

        // Mock BookedTableDto
        BookedTableDto bookedTableDto = new BookedTableDto();
        bookedTableDto.setMovieName("Movie 2");
        bookedTableDto.setBookedSeats(2);

        when(movieRepo.findById("2")).thenReturn(Optional.of(movie));

        // Ensure a TicketException is thrown when seats are not available
        assertThrows(TicketException.class, () -> {
            ticketService.bookTicket(bookedTableDto, "2");
        });

        verify(movieRepo, times(1)).findById("2");
        verify(ticketRepo, never()).save(any(Ticket.class));
    }

    @Test
    public void testBookTicket_MovieNotFound() {
        // Mock BookedTableDto
        BookedTableDto bookedTableDto = new BookedTableDto();
        bookedTableDto.setMovieName("Nonexistent Movie");
        bookedTableDto.setBookedSeats(2);

        when(movieRepo.findById(anyString())).thenReturn(Optional.empty());

        // Ensure a TicketException is thrown when the movie is not found
        assertThrows(TicketException.class, () -> {
            ticketService.bookTicket(bookedTableDto, "123");
        });

        verify(movieRepo, times(1)).findById(anyString());
        verify(ticketRepo, never()).save(any(Ticket.class));
    }

    @Test
    public void testGetTicketTable() {
        // Mock a list of tickets
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        tickets.add(new Ticket());
        when(ticketRepo.findAllByOrderByTransactionIdDesc()).thenReturn(tickets);

        List<Ticket> result = ticketService.getTicketTable();

        verify(ticketRepo, times(1)).findAllByOrderByTransactionIdDesc();
        assertEquals(2, result.size());
    }

}
