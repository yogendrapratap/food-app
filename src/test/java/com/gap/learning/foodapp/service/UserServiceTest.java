package com.gap.learning.foodapp.servicestest;

import com.gap.learning.foodapp.dto.User;
import com.gap.learning.foodapp.dto.UserDTO;
import com.gap.learning.foodapp.dto.UserListDTO;
import com.gap.learning.foodapp.exception.ECommerceAPIValidationException;
import com.gap.learning.foodapp.repository.UserRepository;
import com.gap.learning.foodapp.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsersDetails() {
        User user = new User();
        UserDTO userDTO = new UserDTO();

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserListDTO result = userService.getUsersDetails();

        assertNotNull(result);
        assertEquals(1, result.getUsers().size());
        verify(userRepository).findAll();
    }

    @Test
    void testUserRegistration() {
        UserDTO userDTO = new UserDTO();
        User user = new User();

        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.userRegistration(userDTO);

        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    void testLogin_Success() {
        String username = "testuser";
        String password = "password";
        User user = new User();
        UserDTO userDTO = new UserDTO();

        when(userRepository.findByUsernameAndPassword(username, password)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.login(username, password);

        assertNotNull(result);
        verify(userRepository).findByUsernameAndPassword(username, password);
    }

    @Test
    void testLogin_Failure() {
        String username = "wronguser";
        String password = "wrongpass";

        when(userRepository.findByUsernameAndPassword(username, password)).thenReturn(null);

        ECommerceAPIValidationException exception = assertThrows(ECommerceAPIValidationException.class,
                () -> userService.login(username, password));

        assertEquals("Login failed, Invalid username or password", exception.getMessage());
    }

    @Test
    void testGetUsersDetailsById_Success() {
        Long userId = 1L;
        User user = new User();
        UserDTO userDTO = new UserDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.getUsersDetailsById(userId);

        assertNotNull(result);
        verify(userRepository).findById(userId);
    }

    @Test
    void testGetUsersDetailsById_NotFound() {
        Long userId = 99L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ECommerceAPIValidationException exception = assertThrows(ECommerceAPIValidationException.class,
                () -> userService.getUsersDetailsById(userId));

        assertEquals("Invalid User/User not found", exception.getMessage());
    }
}
