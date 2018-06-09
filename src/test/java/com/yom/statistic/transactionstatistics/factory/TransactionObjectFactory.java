package com.yom.statistic.transactionstatistics.factory;

import com.yom.statistic.transactionstatistics.entity.Transactions;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Setter
@Getter
public class TransactionObjectFactory {


    private TransactionObjectFactory() {
    }

    public static final String ID = "a25c286c-fde7-4291-8cf8-cefee055bfe5";
    public static final Double AMOUNT = 100.00;
    public static final Double AMOUNT_100 = 100.00;
    public static final Double AMOUNT_500 = 500.00;
    public static final Long TIMESTAMP = 1528559452543L;

    public static Transactions getDummyTransactionEntityWithAmount(Double amount) {

        Transactions transaction = new Transactions();
        transaction.setAmount(amount);
        transaction.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusSeconds(60)));
        transaction.setId(ID);
        return transaction;
        
        
    }


}
