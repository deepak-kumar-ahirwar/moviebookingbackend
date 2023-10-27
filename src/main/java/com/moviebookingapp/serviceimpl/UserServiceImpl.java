package com.moviebookingapp.serviceimpl;

import java.security.SecureRandom;
import java.util.*;

import com.moviebookingapp.dto.ForgetPasswordDto;
import com.moviebookingapp.excpetion.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.moviebookingapp.model.User;
import com.moviebookingapp.repository.UserRepository;
import com.moviebookingapp.service.UserService;


@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	UserRepository userRepository;
	int strength = 10;
	BCryptPasswordEncoder bCryptPasswordEncoder =
			new BCryptPasswordEncoder(strength, new SecureRandom());


	@Override
	public ForgetPasswordDto forgetPassword(String userName, ForgetPasswordDto forgetPasswordDto) throws LoginException {
		logger.info("BEGIN - forgetPassword");
		Optional<User> user = userRepository.findByUserName(userName);

		if(user.isPresent()){

			if(forgetPasswordDto.getSecurityQuestion() == user.get().getSecurityQuestion()){

				if(forgetPasswordDto.getSecurityAnswer().equalsIgnoreCase(user.get().getSecurityAnswer())){

					user.get().setPassword(bCryptPasswordEncoder.encode(forgetPasswordDto.getPassword()));
					userRepository.save(user.get());
					logger.info("ENDED - forgetPassword");
					return  forgetPasswordDto;

				}
				logger.error("Security answer is wrong");
				throw new LoginException("Security answer is wrong");
			}
			logger.error("Security question selected is wrong");
			throw new LoginException("Security question selected is wrong");
		}
		logger.error("User not found!!");
		throw new LoginException("User not found!!");
	}

	@Override
	public List<User> getAllUsers() {
		logger.info("BEGIN - getAllUsers");
		List<User> users = new ArrayList<User>();
		userRepository.findAll().forEach(e -> users.add(e));
		logger.info("ENDED - getAllUsers");
		return users;
	}

	@Override
	public User createUser(User user) throws UsernameNotFoundException  {
		logger.info("BEGIN - createUser");
		if (userRepository.findByUserName(user.getUserName()).isPresent()) {
			logger.error("User already exist!!");
			throw new UsernameNotFoundException("User already exist!!");
		}else {
			if(userRepository.findByEmail(user.getEmail()).isPresent()){
				logger.error("Email already exist!!");
				throw new UsernameNotFoundException("Email already exist!!");
			}else{
				user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
				user.setRoleName("User");
				logger.info("ENDED - createUser");
				return userRepository.save(user);
			}

		}



	}

	@Override
	public User findUser(String username) throws UsernameNotFoundException {
		logger.info("BEGIN - createUser");
		if (userRepository.findByUserName(username).isPresent()) {
			logger.info("ENDED - createUser");
			return userRepository.findByUserName(username).get();
		}
		logger.error("User not found!!");
		throw new UsernameNotFoundException("User not found!!");

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("BEGIN - loadUserByUsername");
		if (userRepository.findByUserName(username).isPresent()) {
			User user = userRepository.findByUserName(username).get();
			logger.info("ENDED - loadUserByUsername");
			return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), getAuthority(user));
		}
		logger.error("User not found!!");
		throw new UsernameNotFoundException("User not found!!");
	}

	private Set getAuthority(User user) {
		logger.info("BEGIN - getAuthority");
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRoleName()));
		logger.info("ENDED - getAuthority");
		return authorities;
	}



}
