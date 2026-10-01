package com.agustin.server.services.impl;

import com.agustin.server.domain.entities.User;
import com.agustin.server.dtos.requests.TransactionRequest;
import com.agustin.server.dtos.responses.TransactionDTO;
import com.agustin.server.mappers.TransactionMapper;
import com.agustin.server.repositories.CategoryRepository;
import com.agustin.server.repositories.TransactionRepository;
import com.agustin.server.util.AuthenticatedUserProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;


    @ParameterizedTest
    @CsvSource({
            "true, true",
            "true, false",
            "false, true",
            "false, false"
    })
    void filtraTransacciones(boolean tieneCategoria, boolean tieneFechas) {

        // ARRANGE
        UUID userId = UUID.randomUUID();

        UUID categoryId =
                tieneCategoria ? UUID.randomUUID() : null;

        LocalDateTime inicio =
                tieneFechas
                        ? LocalDateTime.of(2026, 9, 1, 0, 0)
                        : null;

        LocalDateTime fin =
                tieneFechas
                        ? LocalDateTime.of(2026, 9, 30, 23, 59)
                        : null;

        User usuario = mock(User.class);

        when(usuario.getId()).thenReturn(userId);

        when(authenticatedUserProvider.getAuthenticatedUser())
                .thenReturn(usuario);


        if (tieneCategoria && tieneFechas) {

            when(transactionRepository
                    .findByUserIdAndCategoryIdAndCreatedAtBetween(
                            userId,
                            categoryId,
                            inicio,
                            fin
                    ))
                    .thenReturn(List.of());

        } else if (tieneCategoria) {

            when(transactionRepository
                    .findByUserIdAndCategoryId(
                            userId,
                            categoryId
                    ))
                    .thenReturn(List.of());

        } else if (tieneFechas) {

            when(transactionRepository
                    .findByUserIdAndCreatedAtBetween(
                            userId,
                            inicio,
                            fin
                    ))
                    .thenReturn(List.of());

        } else {

            when(transactionRepository
                    .findByUserId(userId))
                    .thenReturn(List.of());
        }


        // ACT
        List<TransactionDTO> resultado =
                transactionService.listTransactions(
                        categoryId,
                        inicio,
                        fin
                );


        // ASSERT
        assertTrue(resultado.isEmpty());


        if (tieneCategoria && tieneFechas) {

            verify(transactionRepository)
                    .findByUserIdAndCategoryIdAndCreatedAtBetween(
                            userId,
                            categoryId,
                            inicio,
                            fin
                    );

        } else if (tieneCategoria) {

            verify(transactionRepository)
                    .findByUserIdAndCategoryId(
                            userId,
                            categoryId
                    );

        } else if (tieneFechas) {

            verify(transactionRepository)
                    .findByUserIdAndCreatedAtBetween(
                            userId,
                            inicio,
                            fin
                    );

        } else {

            verify(transactionRepository)
                    .findByUserId(userId);
        }
    }


    @Test
    void rechazaCategoriaInexistente() {

        // ARRANGE
        UUID categoryId = UUID.randomUUID();

        User usuario = mock(User.class);
        TransactionRequest request = mock(TransactionRequest.class);

        when(authenticatedUserProvider.getAuthenticatedUser())
                .thenReturn(usuario);

        when(request.getCategoryId())
                .thenReturn(categoryId);

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());


        // ACT
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> transactionService.createTransaction(request)
        );


        // ASSERT
        assertEquals(
                "Category not found",
                exception.getMessage()
        );

        verify(transactionRepository, never()).save(any());
    }
}