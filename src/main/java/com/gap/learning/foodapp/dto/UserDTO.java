package com.gap.learning.foodapp.dto;

import java.time.LocalDate;

public class UserDTO {

    private Long id;
    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String phoneNumber;

    private String address;

    private String username;

    private String password;

    public Long getId() {
        return id;
    }

    public UserDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public UserDTO setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserDTO setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public UserDTO setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDTO setPassword(String password) {
        this.password = password;
        return this;
    }
}
