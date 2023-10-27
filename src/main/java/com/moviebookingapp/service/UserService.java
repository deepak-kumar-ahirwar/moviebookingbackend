package com.moviebookingapp.service;

import java.util.List;

import com.moviebookingapp.dto.ForgetPasswordDto;
import com.moviebookingapp.excpetion.LoginException;
import org.springframework.security.core.userdetails.UserDetails;

import com.moviebookingapp.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

	
	List<User> getAllUsers();
	User createUser(User user);

	User findUser(String username);
	ForgetPasswordDto forgetPassword(String username,ForgetPasswordDto forgetPasswordDto) throws LoginException;

	UserDetails loadUserByUsername(String userName);
	
}
