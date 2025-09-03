package com.gap.learning.foodapp.service;

import com.gap.learning.foodapp.dto.User;
import com.gap.learning.foodapp.dto.UserDTO;
import com.gap.learning.foodapp.dto.UserListDTO;
import com.gap.learning.foodapp.exception.ECommerceAPIValidationException;
import com.gap.learning.foodapp.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;


    public UserListDTO getUsersDetails() {

        List<User> users = userRepository.findAll();
        List<UserDTO> userDtos = users.stream().map(u -> modelMapper.map(u, UserDTO.class)).collect(toList());

        return new UserListDTO().setUsers(userDtos);
    }


    public UserDTO userRegistration(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user = userRepository.save(user);
        return modelMapper.map(user, UserDTO.class);
    }


    public UserDTO login(String userName, String password) {

        User user = userRepository.findByUsernameAndPassword(userName, password);
        if (user == null) {
            throw new ECommerceAPIValidationException("Login failed, Invalid username or password");
        }

        return modelMapper.map(user, UserDTO.class);
    }


    public UserDTO getUsersDetailsById(Long id) {

        Optional<User> optionalUser = userRepository.findById(id);

        User user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            throw new ECommerceAPIValidationException("Invalid User/User not found");
        }

        return modelMapper.map(user, UserDTO.class);
    }

}
