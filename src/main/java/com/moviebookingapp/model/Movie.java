package com.moviebookingapp.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Movie {

    @Id
    private String movieId;
    @NotBlank(message = "Movie name is mandatory")
    private String movieName;
    private String theatreName;
    private long seatAvailable;




}
