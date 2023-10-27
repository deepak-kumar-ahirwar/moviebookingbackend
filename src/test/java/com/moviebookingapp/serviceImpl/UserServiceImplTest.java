package com.moviebookingapp.serviceImpl;

import com.moviebookingapp.dto.ForgetPasswordDto;
import com.moviebookingapp.excpetion.LoginException;
import com.moviebookingapp.model.User;
import com.moviebookingapp.repository.UserRepository;
import com.moviebookingapp.serviceimpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;



    @Test
    public void testForgetPassword_ValidCredentials() throws LoginException {
        // Mock an existing user
        User existingUser = new User();
        existingUser.setUserName("john");
        existingUser.setSecurityQuestion(1);
        existingUser.setSecurityAnswer("Answer");

        ForgetPasswordDto forgetPasswordDto = new ForgetPasswordDto();
        forgetPasswordDto.setSecurityQuestion(1);
        forgetPasswordDto.setSecurityAnswer("Answer");
        forgetPasswordDto.setPassword("newPassword");

        when(userRepository.findByUserName("john")).thenReturn(Optional.of(existingUser));
        when(bCryptPasswordEncoder.encode("newPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        ForgetPasswordDto result = userService.forgetPassword("john", forgetPasswordDto);

        verify(userRepository, times(1)).findByUserName("john");
        verify(bCryptPasswordEncoder, times(1)).encode("newPassword");
        verify(userRepository, times(1)).save(any(User.class));

        assertEquals("newPassword", result.getPassword());
    }

    @Test
    public void testForgetPassword_InvalidSecurityQuestion() {
        // Mock an existing user with different security question
        User existingUser = new User();
        existingUser.setUserName("john");
        existingUser.setSecurityQuestion(1);
        existingUser.setSecurityAnswer("Answer");

        ForgetPasswordDto forgetPasswordDto = new ForgetPasswordDto();
        forgetPasswordDto.setSecurityQuestion(2); // Different security question
        forgetPasswordDto.setSecurityAnswer("Answer");
        forgetPasswordDto.setPassword("newPassword");

        when(userRepository.findByUserName("john")).thenReturn(Optional.of(existingUser));

        assertThrows(LoginException.class, () -> {
            userService.forgetPassword("john", forgetPasswordDto);
        });

        verify(userRepository, times(1)).findByUserName("john");
        verifyNoMoreInteractions(bCryptPasswordEncoder);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testForgetPassword_InvalidSecurityAnswer() {
        // Mock an existing user with correct security question but wrong security answer
        User existingUser = new User();
        existingUser.setUserName("john");
        existingUser.setSecurityQuestion(1);
        existingUser.setSecurityAnswer("Answer");

        ForgetPasswordDto forgetPasswordDto = new ForgetPasswordDto();
        forgetPasswordDto.setSecurityQuestion(1);
        forgetPasswordDto.setSecurityAnswer("WrongAnswer"); // Wrong security answer
        forgetPasswordDto.setPassword("newPassword");

        when(userRepository.findByUserName("john")).thenReturn(Optional.of(existingUser));

        assertThrows(LoginException.class, () -> {
            userService.forgetPassword("john", forgetPasswordDto);
        });

        verify(userRepository, times(1)).findByUserName("john");
        verifyNoMoreInteractions(bCryptPasswordEncoder);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testForgetPassword_UserNotFound() {
        // Mock a scenario where the user is not found
        when(userRepository.findByUserName("john")).thenReturn(Optional.empty());

        assertThrows(LoginException.class, () -> {
            userService.forgetPassword("john", new ForgetPasswordDto());
        });

        verify(userRepository, times(1)).findByUserName("john");
        verifyNoMoreInteractions(bCryptPasswordEncoder);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testCreateUser_ValidUser() throws UsernameNotFoundException {
        User newUser = new User();
        newUser.setUserName("newuser");
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("password");

        when(userRepository.findByUserName("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("newuser@example.com")).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User createdUser = userService.createUser(newUser);

        verify(userRepository, times(1)).findByUserName("newuser");
        verify(userRepository, times(1)).findByEmail("newuser@example.com");
        verify(bCryptPasswordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));

        assertEquals("newuser", createdUser.getUserName());
        assertEquals("newuser@example.com", createdUser.getEmail());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals("User", createdUser.getRoleName());
    }

    @Test
    public void testCreateUser_ExistingUserName() {
        User existingUser = new User();
        existingUser.setUserName("existinguser");

        User newUser = new User();
        newUser.setUserName("existinguser");

        when(userRepository.findByUserName("existinguser")).thenReturn(Optional.of(existingUser));

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.createUser(newUser);
        });

        verify(userRepository, times(1)).findByUserName("existinguser");
        verifyNoMoreInteractions(bCryptPasswordEncoder);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testCreateUser_ExistingEmail() {
        User existingUser = new User();
        existingUser.setEmail("existinguser@example.com");

        User newUser = new User();
        newUser.setEmail("existinguser@example.com");

        when(userRepository.findByUserName("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("existinguser@example.com")).thenReturn(Optional.of(existingUser));

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.createUser(newUser);
        });


    }

    @Test
    public void testGetAllUsers() {
        // Mock a list of users
        List<User> userList = new ArrayList<>();
        userList.add(new User("1", "John", "Doe", "john.doe", "User", "john@example.com", "123456", "password", 1, "Answer1"));
        userList.add(new User("2", "Jane", "Smith", "jane.smith", "User", "jane@example.com", "789012", "password", 2, "Answer2"));

        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.getAllUsers();

        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);

        assertEquals(2, result.size());
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        // Mock an existing user
        User existingUser = new User("1", "John", "Doe", "john.doe", "User", "john@example.com", "123456", "password", 1, "Answer1");

        when(userRepository.findByUserName("john.doe")).thenReturn(Optional.of(existingUser));

        UserDetails userDetails = userService.loadUserByUsername("john.doe");



        assertNotNull(userDetails);
        assertEquals("john.doe", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertEquals(Collections.singleton(new SimpleGrantedAuthority("ROLE_User")), userDetails.getAuthorities());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Mock a scenario where the user is not found
        when(userRepository.findByUserName("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonexistent");
        });

        verify(userRepository, times(1)).findByUserName("nonexistent");
        verifyNoMoreInteractions(userRepository);
    }

}
