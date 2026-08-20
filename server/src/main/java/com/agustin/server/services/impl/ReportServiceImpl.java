package com.agustin.server.services.impl;

import com.agustin.server.domain.entities.Transaction;
import com.agustin.server.domain.entities.User;
import com.agustin.server.dtos.responses.MonthlyReportResponse;
import com.agustin.server.repositories.TransactionRepository;
import com.agustin.server.services.ReportService;
import com.agustin.server.util.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

import static com.agustin.server.domain.enums.Type.EXPENSE;
import static com.agustin.server.domain.enums.Type.INCOME;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final TransactionRepository transactionRepository;

    @Override
    public MonthlyReportResponse getMonthlyReport(YearMonth month) {
        User user = authenticatedUserProvider.getAuthenticatedUser();

        LocalDateTime startDate = month.atDay(1).atStartOfDay();
        LocalDateTime endDate = month.atEndOfMonth().atTime(LocalTime.MAX);

        List<Transaction> transactions = transactionRepository
                .findByUserIdAndCreatedAtBetween(user.getId(), startDate, endDate);

        BigDecimal income = transactions.stream()
                .filter(transaction -> transaction.getType() == INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = transactions.stream()
                .filter(transaction -> transaction.getType() == EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new MonthlyReportResponse(month, income, expense, income.subtract(expense));
    }
}
