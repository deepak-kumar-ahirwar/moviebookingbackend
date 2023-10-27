package com.moviebookingapp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgetPasswordDto {
    private String password;
    private int securityQuestion;
    private String securityAnswer;
}
