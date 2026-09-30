package com.agustin.server.services.impl;

import org.junit.jupiter.api.Test;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImplTest {


    private final JwtServiceImpl jwtService =
            new JwtServiceImpl();



    @Test
    void generaTokenCorrectamente() {

        // ARRANGE
        UserDetails usuario =
                User.builder()
                        .username("usuario@mail.com")
                        .password("1234")
                        .authorities("USER")
                        .build();


        // ACT
        String token =
                jwtService.getToken(usuario);


        // ASSERT
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }



    @Test
    void tokenValidoParaUsuarioCorrecto() {

        // ARRANGE
        UserDetails usuario =
                User.builder()
                        .username("usuario@mail.com")
                        .password("1234")
                        .authorities("USER")
                        .build();


        String token =
                jwtService.getToken(usuario);



        // ACT
        boolean resultado =
                jwtService.isTokenValid(token, usuario);



        // ASSERT
        assertTrue(resultado);
    }



    @Test
    void tokenInvalidoParaUsuarioIncorrecto() {

        // ARRANGE
        UserDetails usuarioOriginal =
                User.builder()
                        .username("usuario@mail.com")
                        .password("1234")
                        .authorities("USER")
                        .build();


        UserDetails otroUsuario =
                User.builder()
                        .username("otro@mail.com")
                        .password("1234")
                        .authorities("USER")
                        .build();


        String token =
                jwtService.getToken(usuarioOriginal);



        // ACT
        boolean resultado =
                jwtService.isTokenValid(token, otroUsuario);



        // ASSERT
        assertFalse(resultado);
    }
}