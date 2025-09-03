package com.gap.learning.foodapp.dto;

import java.util.List;

public class UserListDTO {

    private List<UserDTO> users;

    public List<UserDTO> getUsers() {
        return users;
    }

    public UserListDTO setUsers(List<UserDTO> users) {
        this.users = users;
        return this;
    }
}
