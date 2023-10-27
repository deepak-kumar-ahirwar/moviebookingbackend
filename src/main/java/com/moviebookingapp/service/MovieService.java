package com.moviebookingapp.service;

import java.util.List;

import com.moviebookingapp.model.Movie;


public interface MovieService {

    Movie updateMovie(String  id,Movie movie);
    Movie addMovie(Movie movie);
List<Movie> getAllMovies();

    Movie deleteMovie(String id);

Movie searchMovieById(String id);
List<Movie> searchMovieByName(String name);

}
