package com.moviebookingapp;

import com.moviebookingapp.controller.MovieController;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Movie Booking App API", version = "1.0", description = "Movie Booking App API"))
@SecurityScheme( name = "Bearer Authentication",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		scheme = "bearer")
public class MovieBookingApplication {

   private static Logger logger = LogManager.getLogger(MovieBookingApplication.class);
	public static void main(String[] args) {

		logger.info("Movie booking app running.......");
		SpringApplication.run(MovieBookingApplication.class, args);
	}

}
