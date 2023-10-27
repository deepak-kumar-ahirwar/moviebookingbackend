package com.moviebookingapp.serviceImpl;

import com.moviebookingapp.excpetion.CustomMessageException;
import com.moviebookingapp.model.Movie;
import com.moviebookingapp.repository.MovieRepo;
import com.moviebookingapp.serviceimpl.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class MovieServiceImplTest {

    @InjectMocks
    private MovieServiceImpl movieService;

    @Mock
    private MovieRepo movieRepo;

    @Test
    public void testUpdateMovie_ExistingMovie() {
        // Mock an existing movie with ID "movie123"
        Movie existingMovie = new Movie();
        existingMovie.setMovieId("movie123");
        existingMovie.setMovieName("Old Movie Name");
        existingMovie.setTheatreName("Old Theatre Name");
        existingMovie.setSeatAvailable(50);

        // Create a new movie with updated details
        Movie updatedMovie = new Movie();
        updatedMovie.setMovieName("New Movie Name");
        updatedMovie.setTheatreName("New Theatre Name");
        updatedMovie.setSeatAvailable(75);

        when(movieRepo.findById("movie123")).thenReturn(Optional.of(existingMovie));
        when(movieRepo.save(existingMovie)).thenReturn(existingMovie);

        Movie result = movieService.updateMovie("movie123", updatedMovie);

        verify(movieRepo, times(1)).findById("movie123");
        verify(movieRepo, times(1)).save(existingMovie);

        assertEquals("movie123", result.getMovieId());
        assertEquals("New Movie Name", result.getMovieName());
        assertEquals("New Theatre Name", result.getTheatreName());
        assertEquals(75, result.getSeatAvailable());
    }

    @Test
    public void testUpdateMovie_NonexistentMovie() {
        // Create a new movie with updated details
        Movie updatedMovie = new Movie();
        updatedMovie.setMovieName("New Movie Name");
        updatedMovie.setTheatreName("New Theatre Name");
        updatedMovie.setSeatAvailable(75);

        when(movieRepo.findById("movie123")).thenReturn(Optional.empty());

        // Ensure a CustomMessageException is thrown when the movie is not found
        assertThrows(CustomMessageException.class, () -> {
            movieService.updateMovie("movie123", updatedMovie);
        });

        verify(movieRepo, times(1)).findById("movie123");
        verify(movieRepo, never()).save(any(Movie.class));
    }



    @Test
    public void testGetAllMovies() {
        // Mock a list of movies
        List<Movie> movieList = new ArrayList<>();
        Movie movie1 = new Movie();
        Movie movie2 = new Movie();
        movieList.add(movie1);
        movieList.add(movie2);

        when(movieRepo.findAll()).thenReturn(movieList);

        List<Movie> result = movieService.getAllMovies();

        verify(movieRepo, times(1)).findAll();

        assertEquals(2, result.size()); // Ensure that the correct number of movies is returned
    }

    @Test
    public void testDeleteMovie_ExistingMovie() {
        // Mock an existing movie with ID "movie123"
        Movie existingMovie = new Movie();
        existingMovie.setMovieId("movie123");

        when(movieRepo.findById("movie123")).thenReturn(Optional.of(existingMovie));

        Movie result = movieService.deleteMovie("movie123");

        verify(movieRepo, times(1)).findById("movie123");
        verify(movieRepo, times(1)).deleteById("movie123");

        assertEquals("movie123", result.getMovieId()); // Ensure the correct movie is deleted
    }

    @Test
    public void testDeleteMovie_NonexistentMovie() {
        when(movieRepo.findById("movie123")).thenReturn(Optional.empty());

        // Ensure a CustomMessageException is thrown when the movie is not found
        assertThrows(CustomMessageException.class, () -> {
            movieService.deleteMovie("movie123");
        });

        verify(movieRepo, times(1)).findById("movie123");
        verify(movieRepo, never()).deleteById(anyString());
    }

    @Test
    public void testSearchMovieById_ExistingMovie() {
        // Mock an existing movie with ID "movie123"
        Movie existingMovie = new Movie();
        existingMovie.setMovieId("movie123");

        when(movieRepo.findById("movie123")).thenReturn(Optional.of(existingMovie));

        Movie result = movieService.searchMovieById("movie123");

        verify(movieRepo, times(1)).findById("movie123");

        assertEquals("movie123", result.getMovieId());
    }



    @Test
    public void testSearchMovieByName_MatchingMovies() {
        // Mock a list of movies with names containing "Avengers"
        List<Movie> movieList = new ArrayList<>();
        Movie movie1 = new Movie();
        movie1.setMovieName("Avengers: Endgame");
        Movie movie2 = new Movie();
        movie2.setMovieName("Avengers: Infinity War");
        movieList.add(movie1);
        movieList.add(movie2);

        when(movieRepo.findAllByMovieNameIgnoreCaseContaining("Avengers")).thenReturn(movieList);

        List<Movie> result = movieService.searchMovieByName("Avengers");

        verify(movieRepo, times(1)).findAllByMovieNameIgnoreCaseContaining("Avengers");

        assertEquals(2, result.size()); // Ensure that both matching movies are returned
    }

    @Test
    public void testSearchMovieByName_NoMatchingMovies() {
        // Mock an empty list of movies
        List<Movie> emptyList = new ArrayList<>();

        when(movieRepo.findAllByMovieNameIgnoreCaseContaining("Nonexistent")).thenReturn(emptyList);

        List<Movie> result = movieService.searchMovieByName("Nonexistent");

        verify(movieRepo, times(1)).findAllByMovieNameIgnoreCaseContaining("Nonexistent");

        assertEquals(0, result.size()); // Ensure an empty list is returned
    }
    @Test
    public void testAddMovie_ValidMovie() {
        // Create a new movie
        Movie newMovie = new Movie();
        newMovie.setMovieName("New Movie Name");
        newMovie.setTheatreName("New Theatre Name");

        when(movieRepo.save(any(Movie.class))).thenReturn(newMovie);

        Movie result = movieService.addMovie(newMovie);

        verify(movieRepo, times(1)).save(any(Movie.class));


        assertEquals("New Movie Name", result.getMovieName());
        assertEquals("New Theatre Name", result.getTheatreName());
        assertEquals(100, result.getSeatAvailable());


    }

}
