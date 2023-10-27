package com.moviebookingapp.serviceimpl;


import java.util.List;
import java.util.Optional;

import com.moviebookingapp.kafka.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviebookingapp.dto.BookedTableDto;
import com.moviebookingapp.excpetion.TicketException;
import com.moviebookingapp.model.Movie;
import com.moviebookingapp.model.Ticket;
import com.moviebookingapp.repository.MovieRepo;
import com.moviebookingapp.repository.TicketRepo;
import com.moviebookingapp.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService {

	private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Autowired
    MovieRepo movieRepo;
    @Autowired
    TicketRepo ticketRepo;

	@Autowired
	KafkaProducer kafkaProducer;

    @Override
    public Ticket bookTicket(BookedTableDto bookedTableDto,String movieId) throws TicketException {
		logger.info("BEGIN - bookTicket");
    	Optional<Movie> movie = movieRepo.findById(movieId);
    	if(movie.isPresent()) {
    		if(movie.get().getSeatAvailable() >= bookedTableDto.getBookedSeats()) {
    			Ticket ticket = new Ticket();
        		final long avaibaleSeat = movie.get().getSeatAvailable()- bookedTableDto.getBookedSeats();
        		movie.get().setSeatAvailable(avaibaleSeat);
        		ticket.setAvailableSeats(avaibaleSeat);
        		ticket.setMovieName(bookedTableDto.getMovieName());
        		ticket.setBookedSeats(bookedTableDto.getBookedSeats());
        		ticket.setSeatNumber(bookedTableDto.getSeatNumber());
        		ticket.setTotalseats(100);
        		movieRepo.save(movie.get());
//				kafkaProducer.sendMessage(bookedTableDto.getBookedSeats()+" Ticket Booked for "+bookedTableDto.getMovieName());
				logger.info("ENDED - bookTicket");
        		return ticketRepo.save(ticket);
    		}
			logger.error("Please enter only available seats");
    		throw new TicketException("Please enter only available seats");
    	}
		logger.error("Movie not found");
    	throw new TicketException("Movie not found");

    }

    @Override
    public List<Ticket> getTicketTable() {
		logger.info("BEGIN - getTicketTable");
        return ticketRepo.findAllByOrderByTransactionIdDesc();
    }

}
