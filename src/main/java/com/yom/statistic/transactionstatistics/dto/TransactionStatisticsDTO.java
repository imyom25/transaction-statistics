package com.yom.statistic.transactionstatistics.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TransactionStatisticsDTO implements Serializable {

    private static final long serialVersionUID = 27L;

    private double sum;

    private double avg;

    private double max;

    private double min;

    private long count;

}
