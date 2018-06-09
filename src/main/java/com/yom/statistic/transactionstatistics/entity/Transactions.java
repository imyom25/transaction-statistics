package com.yom.statistic.transactionstatistics.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
public class Transactions implements Serializable {

    private static final long serialVersionUID = 26L;

    @Id
    private String id;

    private Double amount;

    private Timestamp timestamp;

}
