package com.yom.statistic.transactionstatistics.respository;

import com.yom.statistic.transactionstatistics.entity.Transactions;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, String>{

    @Cacheable(cacheNames = "paymentInfo", sync = true)
    List<Transactions> findAllByTimestampAfter(Timestamp currentTime);
}
