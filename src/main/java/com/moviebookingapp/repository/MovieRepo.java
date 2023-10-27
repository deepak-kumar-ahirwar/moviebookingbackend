package com.moviebookingapp.repository;

import com.moviebookingapp.model.Movie;

import java.util.List;


import com.moviebookingapp.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepo extends MongoRepository<Movie,String> {

	List<Movie> findAllByMovieNameIgnoreCaseContaining(String name);
}
