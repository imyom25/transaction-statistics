package com.yom.statistic.transactionstatistics.service.impl;

import com.yom.statistic.transactionstatistics.dto.TransactionStatisticsDTO;
import com.yom.statistic.transactionstatistics.entity.Transactions;
import com.yom.statistic.transactionstatistics.respository.TransactionRepository;
import com.yom.statistic.transactionstatistics.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.DoubleSummaryStatistics;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.yom.statistic.transactionstatistics.constant.TransactionConstants.SECONDS;

@CacheConfig(cacheNames = "payments")
@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void createTransaction(Double amount, long timestamp) {

        Transactions transactions = new Transactions();
        transactions.setId(UUID.randomUUID().toString());
        transactions.setAmount(amount);
        transactions.setTimestamp(new Timestamp(timestamp));

        transactionRepository.save(transactions);
    }

    @Override
    public TransactionStatisticsDTO getTransactionStatistics() {

        DoubleSummaryStatistics doubleSummaryStatistics = transactionRepository.findAllByTimestampAfter(Timestamp.valueOf
                (LocalDateTime.now(ZoneId.systemDefault()).minusSeconds(SECONDS)))
                .stream()
                .collect(Collectors.summarizingDouble(Transactions::getAmount));

        return buildTransactionStatisticsDTO(doubleSummaryStatistics);

    }

    private TransactionStatisticsDTO buildTransactionStatisticsDTO(DoubleSummaryStatistics doubleSummaryStatistics) {

        TransactionStatisticsDTO transactionStatisticsDTO = new TransactionStatisticsDTO();
        if(doubleSummaryStatistics.getCount()>0){
            transactionStatisticsDTO.setSum(doubleSummaryStatistics.getSum());
            transactionStatisticsDTO.setAvg(doubleSummaryStatistics.getAverage());
            transactionStatisticsDTO.setMin(doubleSummaryStatistics.getMin());
            transactionStatisticsDTO.setMax(doubleSummaryStatistics.getMax());
            transactionStatisticsDTO.setCount(doubleSummaryStatistics.getCount());
            return transactionStatisticsDTO;
        }
        return transactionStatisticsDTO;
    }
}
