package com.agustin.server.services.impl;

import com.agustin.server.domain.entities.User;
import com.agustin.server.dtos.responses.UserDTO;
import com.agustin.server.dtos.requests.UserRequest;
import com.agustin.server.mappers.UserMapper;
import com.agustin.server.repositories.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;


    @InjectMocks
    private UserServiceImpl userService;



    @Test
    void perfilUsuarioExistente() {

        // ARRANGE
        String email = "usuario@mail.com";

        User usuario = mock(User.class);
        UserDTO dto = mock(UserDTO.class);


        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(usuario));

        when(userMapper.toDto(usuario))
                .thenReturn(dto);



        // ACT
        UserDTO resultado =
                userService.getProfile(email);



        // ASSERT
        assertSame(dto, resultado);

        verify(userRepository)
                .findByEmail(email);

        verify(userMapper)
                .toDto(usuario);
    }



    @Test
    void perfilUsuarioInexistente() {

        // ARRANGE
        String email = "noexiste@mail.com";


        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());



        // ACT
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.getProfile(email)
                );



        // ASSERT
        assertEquals(
                "User not found",
                exception.getMessage()
        );


        verify(userMapper, never())
                .toDto(any());
    }
}