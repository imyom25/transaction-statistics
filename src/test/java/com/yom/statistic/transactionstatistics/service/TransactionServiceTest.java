package com.yom.statistic.transactionstatistics.service;


import com.yom.statistic.transactionstatistics.dto.TransactionStatisticsDTO;
import com.yom.statistic.transactionstatistics.entity.Transactions;
import com.yom.statistic.transactionstatistics.respository.TransactionRepository;
import com.yom.statistic.transactionstatistics.service.impl.TransactionServiceImpl;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;

import static com.yom.statistic.transactionstatistics.factory.TransactionObjectFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService = new TransactionServiceImpl(transactionRepository);

    @Captor
    private ArgumentCaptor<Transactions> transactionsArgumentCaptor;

    @Captor
    private ArgumentCaptor<Timestamp> timestampArgumentCaptor;

    @Test
    public void givenAmountAndTimestamp_ThenShouldSuccessfullyCreateTransaction() {

        //WHEN
        when(transactionRepository.save(any(Transactions.class))).thenReturn(new Transactions());
        //THEN
        transactionService.createTransaction(AMOUNT, TIMESTAMP);

        //VERIFY
        verify(transactionRepository).save(transactionsArgumentCaptor.capture());

        Transactions transactions = transactionsArgumentCaptor.getValue();
        assertThat(transactions.getAmount()).isEqualTo(AMOUNT);
        assertThat(transactions.getId()).isInstanceOf(String.class);
        assertThat(transactions.getTimestamp()).isInThePast();
    }

    @Test
    public void shouldReturnTransactionStatistics_WhenNoEligibleTransactionsFound() {

        //WHEN
        when(transactionRepository.findAllByTimestampAfter(any(Timestamp.class))).thenReturn(Lists.newArrayList());
        //THEN
        TransactionStatisticsDTO transactionStatisticsData = transactionService.getTransactionStatistics();

        assertThat(transactionStatisticsData).isNotNull();
        assertThat(transactionStatisticsData.getSum()).isZero();
        assertThat(transactionStatisticsData.getAvg()).isZero();
        assertThat(transactionStatisticsData.getCount()).isZero();
        assertThat(transactionStatisticsData.getMax()).isZero();
        assertThat(transactionStatisticsData.getMin()).isZero();

        //VERIFY
        verify(transactionRepository).findAllByTimestampAfter(timestampArgumentCaptor.capture());
        Timestamp timestampInRequest = timestampArgumentCaptor.getValue();
        assertThat(timestampInRequest).isToday();
    }

    @Test
    public void shouldReturnTransactionStatistics_WhenEligibleTransactionsFound() {

        //GIVEN
        Transactions transactions_one = getDummyTransactionEntityWithAmount(AMOUNT_100);
        Transactions transactions_two = getDummyTransactionEntityWithAmount(AMOUNT_500);

        //WHEN
        when(transactionRepository.findAllByTimestampAfter(any(Timestamp.class)))
                .thenReturn(Lists.newArrayList(transactions_one, transactions_two));

        //THEN
        TransactionStatisticsDTO transactionStatisticsData = transactionService.getTransactionStatistics();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(transactionStatisticsData).isNotNull();
        softly.assertThat(transactionStatisticsData.getSum()).isEqualTo(600.0);
        softly.assertThat(transactionStatisticsData.getAvg()).isEqualTo(300.0);
        softly.assertThat(transactionStatisticsData.getCount()).isEqualTo(2);
        softly.assertThat(transactionStatisticsData.getMax()).isEqualTo(500.0);
        softly.assertThat(transactionStatisticsData.getMin()).isEqualTo(100.0);

        //VERIFY
        verify(transactionRepository).findAllByTimestampAfter(timestampArgumentCaptor.capture());
        Timestamp timestampInRequest = timestampArgumentCaptor.getValue();
        softly.assertThat(timestampInRequest).isToday();

        softly.assertAll();
    }
}
