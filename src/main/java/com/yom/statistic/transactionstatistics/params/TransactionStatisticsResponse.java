package com.yom.statistic.transactionstatistics.params;

import lombok.Data;


@Data
public class TransactionStatisticsResponse {

    private double sum;

    private double avg;

    private double max;

    private double min;

    private long count;

}
