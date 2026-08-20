package com.agustin.server.services;

import com.agustin.server.domain.entities.User;
import com.agustin.server.dtos.requests.LoginRequest;
import com.agustin.server.dtos.requests.UserRequest;
import com.agustin.server.dtos.responses.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserDTO> getAllUsers();
    UserDTO getProfile(String email);
    UserDTO updateUser(UUID id, UserRequest request);
    String deleteUser(UUID id);
}