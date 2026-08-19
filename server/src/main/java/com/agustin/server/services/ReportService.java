package com.agustin.server.services;

import com.agustin.server.dtos.responses.MonthlyReportResponse;

import java.time.YearMonth;

public interface ReportService {
    MonthlyReportResponse getMonthlyReport(YearMonth month);
}
