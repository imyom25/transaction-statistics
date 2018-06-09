package com.yom.statistic.transactionstatistics.service;

import com.yom.statistic.transactionstatistics.dto.TransactionStatisticsDTO;

public interface TransactionService {

    void createTransaction(Double amount, long timestamp);

    TransactionStatisticsDTO getTransactionStatistics();
}
