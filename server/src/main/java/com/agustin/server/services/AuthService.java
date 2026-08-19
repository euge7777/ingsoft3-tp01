package com.agustin.server.services;

import com.agustin.server.dtos.requests.LoginRequest;
import com.agustin.server.dtos.requests.RegisterRequest;
import com.agustin.server.dtos.responses.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
}
