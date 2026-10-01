package com.agustin.server.services.impl;

import com.agustin.server.domain.entities.User;
import com.agustin.server.dtos.requests.RegisterRequest;
import com.agustin.server.repositories.UserRepository;
import com.agustin.server.services.JwtService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;


    @Test
    void registroConEmailDisponible() {

        // ARRANGE
        RegisterRequest request = mock(RegisterRequest.class);

        when(request.getEmail()).thenReturn("test@mail.com");
        when(request.getFirstName()).thenReturn("Juan");
        when(request.getLastName()).thenReturn("Perez");
        when(request.getPassword()).thenReturn("1234");

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("1234"))
                .thenReturn("password-codificada");

        when(jwtService.getToken(any(User.class)))
                .thenReturn("token-prueba");


        // ACT
        authService.register(request);


        // ASSERT
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("1234");
        verify(jwtService).getToken(any(User.class));
    }


    @Test
    void registroConEmailDuplicado() {

        // ARRANGE
        RegisterRequest request = mock(RegisterRequest.class);
        User usuarioExistente = mock(User.class);

        when(request.getEmail()).thenReturn("repetido@mail.com");

        when(userRepository.findByEmail("repetido@mail.com"))
                .thenReturn(Optional.of(usuarioExistente));


        // ACT
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(request)
        );


        // ASSERT
        assertEquals(
                "Email is already in use",
                exception.getMessage()
        );

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
        verify(jwtService, never()).getToken(any());
    }
}