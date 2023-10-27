package com.moviebookingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private String userId;
    private String userName;
    private String roleName;
    private String firstName;
    private String lastName;
}
