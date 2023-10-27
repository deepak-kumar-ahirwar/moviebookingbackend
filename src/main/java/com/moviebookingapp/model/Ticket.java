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

public class Ticket {

    @Id
    private String transactionId;
    @NotBlank(message = "Movie Name is mandatory")
    private String movieName;
    private long totalseats;
    private long availableSeats;
    private long bookedSeats;
    private String seatNumber;

}
