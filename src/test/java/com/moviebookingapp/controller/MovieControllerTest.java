package com.moviebookingapp.controller;


import com.moviebookingapp.model.Movie;
import com.moviebookingapp.service.MovieService;
import com.moviebookingapp.serviceimpl.MovieServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MovieControllerTest {

    @InjectMocks
    private MovieController movieController;

    @Mock
    private MovieService movieService;


    @Test
    public void testGetAllMovie() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie());
        when(movieService.getAllMovies()).thenReturn(movies);

        ResponseEntity<List<Movie>> response = movieController.getAllMovie();

        verify(movieService, times(1)).getAllMovies();
        assert(response.getStatusCode() == HttpStatus.OK);
        assert(response.getBody() != null);
        assert(response.getBody().size() == 1); // Check if one movie is returned
    }

    @Test
    public void testUpdateMovie() {
        Movie updatedMovie = new Movie();
        String movieId = "1";

        when(movieService.updateMovie(eq(movieId), any(Movie.class))).thenReturn(updatedMovie);

        ResponseEntity<Movie> response = movieController.updateMovie(movieId, new Movie());

        verify(movieService, times(1)).updateMovie(eq(movieId), any(Movie.class));
        assert(response.getStatusCode() == HttpStatus.OK);
        assert(response.getBody() != null);
        assert(response.getBody() == updatedMovie);
    }

    @Test
    public void testAddMovie() {
        Movie newMovie = new Movie();

        when(movieService.addMovie(any(Movie.class))).thenReturn(newMovie);

        ResponseEntity<Movie> response = movieController.addMovie(newMovie);

        verify(movieService, times(1)).addMovie(any(Movie.class));
        assert(response.getStatusCode() == HttpStatus.OK);
        assert(response.getBody() != null);
        assert(response.getBody() == newMovie);
    }

    @Test
    public void testDeleteMovie() {
        String movieId = "1";

        when(movieService.deleteMovie(eq(movieId))).thenReturn(new Movie());

        ResponseEntity<Movie> response = movieController.deleteMovie(movieId);

        verify(movieService, times(1)).deleteMovie(eq(movieId));
        assert(response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void testSearchMovieById() {
        String movieId = "1";
        Movie movie = new Movie();

        when(movieService.searchMovieById(eq(movieId))).thenReturn(movie);

        ResponseEntity<Movie> response = movieController.searchMovieBYId(movieId);

        verify(movieService, times(1)).searchMovieById(eq(movieId));
        assert(response.getStatusCode() == HttpStatus.OK);
        assert(response.getBody() != null);
        assert(response.getBody() == movie);
    }

    @Test
    public void testSearchMovieByName() {
        String movieName = "MovieName";
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie());

        when(movieService.searchMovieByName(eq(movieName))).thenReturn(movies);

        ResponseEntity<List<Movie>> response = movieController.searchMovieByName(movieName);

        verify(movieService, times(1)).searchMovieByName(eq(movieName));
        assert(response.getStatusCode() == HttpStatus.OK);
        assert(response.getBody() != null);
        assert(response.getBody().size() == 1); // Check if one movie is returned
    }
}
