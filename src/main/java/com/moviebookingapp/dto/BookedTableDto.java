package com.moviebookingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookedTableDto {
    private String movieName;
    private long bookedSeats;
    private String  seatNumber;


}
