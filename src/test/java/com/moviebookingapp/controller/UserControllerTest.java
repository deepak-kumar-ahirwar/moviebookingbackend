package com.moviebookingapp.controller;

import com.moviebookingapp.dto.AuthRequestDto;
import com.moviebookingapp.dto.ForgetPasswordDto;
import com.moviebookingapp.excpetion.LoginException;
import com.moviebookingapp.model.User;
import com.moviebookingapp.repository.UserRepository;
import com.moviebookingapp.service.UserService;
import com.moviebookingapp.util.JwtUtil;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtTokenUtil;

    @Mock
    private AuthenticationManager authenticationManager;



    @Test
    public void testCreateUser() {
        User user = new User();
        when(userService.createUser(any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.createUser(user);

        verify(userService, times(1)).createUser(any(User.class));
        assert(response.getStatusCode() == HttpStatus.CREATED);
        assert(response.getBody() != null);
        assert(response.getBody() == user);
    }

    @Test
    public void testForgetPassword() throws LoginException {
        String userName = "username";
        User user = new User();
        when(userService.findUser(eq(userName))).thenReturn(user);

        ResponseEntity<User> response = userController.forgetPassword(userName);

        verify(userService, times(1)).findUser(eq(userName));
        assert(response.getStatusCode() == HttpStatus.CREATED);
        assert(response.getBody() != null);
        assert(response.getBody() == user);
    }

    @Test
    public void testForgetPasswordDto() throws LoginException {
        String userName = "username";
        ForgetPasswordDto forgetPasswordDto = new ForgetPasswordDto();
        when(userService.forgetPassword(eq(userName), any(ForgetPasswordDto.class))).thenReturn(forgetPasswordDto);

        ResponseEntity<ForgetPasswordDto> response = userController.forgetPassword(userName, forgetPasswordDto);

        verify(userService, times(1)).forgetPassword(eq(userName), any(ForgetPasswordDto.class));
        assert(response.getStatusCode() == HttpStatus.CREATED);
        assert(response.getBody() != null);
        assert(response.getBody() == forgetPasswordDto);
    }

    @Test
    public void testCreateAuthorizationToken_InvalidCredentials() throws AuthenticationException, LoginException {
        AuthRequestDto authReq = new AuthRequestDto();
        authReq.setUsername("username");
        authReq.setPassword("invalid_password");

        when(authenticationManager.authenticate(any())).thenThrow(new UsernameNotFoundException("User not found"));
        assertThrows(LoginException.class,()-> userController.createAuthorizationToken(authReq));


    }

    @Test
    public void testCreateAuthorizationToken_UserNotFound() throws AuthenticationException, LoginException {
        AuthRequestDto authReq = new AuthRequestDto();
        authReq.setUsername("nonexistent_user");
        authReq.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        UserDetails userDetails = mock(UserDetails.class);
        when(userService.loadUserByUsername(eq(authReq.getUsername()))).thenReturn(userDetails);

        when(userRepository.findByUserName(eq(authReq.getUsername()))).thenReturn(Optional.empty());

        assertThrows(LoginException.class,()->userController.createAuthorizationToken(authReq));

    }

}
