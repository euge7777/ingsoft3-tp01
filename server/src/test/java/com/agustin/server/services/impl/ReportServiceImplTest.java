package com.agustin.server.services.impl;

import com.agustin.server.domain.entities.Transaction;
import com.agustin.server.domain.entities.User;
import com.agustin.server.dtos.responses.MonthlyReportResponse;
import com.agustin.server.repositories.TransactionRepository;
import com.agustin.server.util.AuthenticatedUserProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

import static com.agustin.server.domain.enums.Type.EXPENSE;
import static com.agustin.server.domain.enums.Type.INCOME;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private ReportServiceImpl reportService;


    @Test
    void calculaBalanceMensual() {

        // ARRANGE
        UUID userId = UUID.randomUUID();
        YearMonth mes = YearMonth.of(2026, 9);

        User usuario = mock(User.class);

        Transaction ingreso1 = mock(Transaction.class);
        Transaction ingreso2 = mock(Transaction.class);
        Transaction gasto = mock(Transaction.class);

        when(usuario.getId()).thenReturn(userId);

        when(authenticatedUserProvider.getAuthenticatedUser())
                .thenReturn(usuario);

        when(ingreso1.getType()).thenReturn(INCOME);
        when(ingreso1.getAmount()).thenReturn(new BigDecimal("1000"));

        when(ingreso2.getType()).thenReturn(INCOME);
        when(ingreso2.getAmount()).thenReturn(new BigDecimal("500"));

        when(gasto.getType()).thenReturn(EXPENSE);
        when(gasto.getAmount()).thenReturn(new BigDecimal("300"));

        when(transactionRepository.findByUserIdAndCreatedAtBetween(
                userId,
                mes.atDay(1).atStartOfDay(),
                mes.atEndOfMonth().atTime(LocalTime.MAX)
        )).thenReturn(List.of(ingreso1, ingreso2, gasto));


        // ACT
        MonthlyReportResponse resultado =
                reportService.getMonthlyReport(mes);


        // ASSERT
        assertEquals(new BigDecimal("1500"), resultado.getTotalIncome());
        assertEquals(new BigDecimal("300"), resultado.getTotalExpense());
        assertEquals(new BigDecimal("1200"), resultado.getBalance());
        assertEquals(mes, resultado.getMonth());
    }


    @Test
    void reporteSinMovimientos() {

        // ARRANGE
        UUID userId = UUID.randomUUID();
        YearMonth mes = YearMonth.of(2026, 9);

        User usuario = mock(User.class);

        when(usuario.getId()).thenReturn(userId);

        when(authenticatedUserProvider.getAuthenticatedUser())
                .thenReturn(usuario);

        when(transactionRepository.findByUserIdAndCreatedAtBetween(
                userId,
                mes.atDay(1).atStartOfDay(),
                mes.atEndOfMonth().atTime(LocalTime.MAX)
        )).thenReturn(List.of());


        // ACT
        MonthlyReportResponse resultado =
                reportService.getMonthlyReport(mes);


        // ASSERT
        assertEquals(BigDecimal.ZERO, resultado.getTotalIncome());
        assertEquals(BigDecimal.ZERO, resultado.getTotalExpense());
        assertEquals(BigDecimal.ZERO, resultado.getBalance());
    }
}