package com.yom.statistic.transactionstatistics.params;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TransactionRequest {

    @NotNull
    private Long timestamp;

    @NotNull
    private Double amount;

}
