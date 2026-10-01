package com.agustin.server.util;

import com.agustin.server.domain.entities.User;
import com.agustin.server.repositories.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthenticatedUserProviderTest {


    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private AuthenticatedUserProvider userProvider;



    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }



    @Test
    void usuarioAutenticadoDevuelveUsuario() {

        // ARRANGE
        User usuario = mock(User.class);

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername("usuario@mail.com")
                        .password("1234")
                        .authorities("USER")
                        .build();


        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        )
                );


        when(userRepository.findByEmail("usuario@mail.com"))
                .thenReturn(Optional.of(usuario));



        // ACT
        User resultado =
                userProvider.getAuthenticatedUser();



        // ASSERT
        assertSame(usuario, resultado);

        verify(userRepository)
                .findByEmail("usuario@mail.com");
    }



    @Test
    void usuarioNoAutenticadoLanzaError() {


        // ACT
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> userProvider.getAuthenticatedUser()
                );


        // ASSERT
        assertEquals(
                "User not found",
                exception.getMessage()
        );


        verify(userRepository, never())
                .findByEmail(anyString());
    }
}