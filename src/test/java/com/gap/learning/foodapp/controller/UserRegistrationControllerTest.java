package com.gap.learning.foodapp.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gap.learning.foodapp.dto.UserDTO;
import com.gap.learning.foodapp.dto.UserListDTO;
import com.gap.learning.foodapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRegistrationController.class)
class UserRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetUsersDetails() throws Exception {
        UserDTO user = new UserDTO()
                .setId(1L)
                .setFirstName("John")
                .setLastName("Doe")
                .setDateOfBirth(LocalDate.of(1990, 1, 1))
                .setPhoneNumber("1234567890")
                .setAddress("Test Address")
                .setUsername("johndoe")
                .setPassword("secret");

        UserListDTO userListDTO = new UserListDTO().setUsers(Collections.singletonList(user));

        when(userService.getUsersDetails()).thenReturn(userListDTO);

        mockMvc.perform(get("/user/details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].firstName").value("John"));
    }

    @Test
    void testGetUserDetailsById() throws Exception {
        Long userId = 2L;

        UserDTO user = new UserDTO()
                .setId(userId)
                .setFirstName("Jane")
                .setLastName("Smith")
                .setDateOfBirth(LocalDate.of(1985, 5, 10))
                .setPhoneNumber("9876543210")
                .setAddress("Somewhere")
                .setUsername("janesmith")
                .setPassword("123456");

        when(userService.getUsersDetailsById(userId)).thenReturn(user);

        mockMvc.perform(get("/user/details/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void testUserRegistration() throws Exception {
        UserDTO user = createUser();

        when(userService.userRegistration(any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(post("/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.username").value("johndoe"));
    }

    private UserDTO createUser() {
        return new UserDTO()
                .setId(1L)
                .setFirstName("John")
                .setLastName("Doe")
                .setDateOfBirth(LocalDate.of(1990, 1, 1))
                .setPhoneNumber("1234567890")
                .setAddress("123 Street")
                .setUsername("johndoe")
                .setPassword("password123");
    }

    @Test
    void testUserLogin() throws Exception {
        when(userService.login("naitik", "chauhan"))
                .thenReturn(new UserDTO());

        mockMvc.perform(get("/user/login")
                        .param("userName", "naitik")
                        .param("password", "chauhan"))
                .andExpect(status().isOk())
                .andExpect(content().string("User login successful"));
    }
}

