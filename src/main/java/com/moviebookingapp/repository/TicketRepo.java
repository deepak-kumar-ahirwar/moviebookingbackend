package com.moviebookingapp.repository;


import com.moviebookingapp.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepo extends MongoRepository<Ticket,String> {



	List<Ticket> findAllByOrderByTransactionIdDesc();


}
