package com.yom.statistic.transactionstatistics.controller;

import com.yom.statistic.transactionstatistics.dto.TransactionStatisticsDTO;
import com.yom.statistic.transactionstatistics.params.TransactionRequest;
import com.yom.statistic.transactionstatistics.params.TransactionStatisticsResponse;
import com.yom.statistic.transactionstatistics.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.yom.statistic.transactionstatistics.constant.TransactionConstants.TIME_LIMIT;
import static java.lang.System.currentTimeMillis;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CacheConfig(cacheNames = "payments")
@RestController
public class TransactionController {

    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostConstruct
    @CacheEvict(allEntries = true)
    public void clearCache() {
    }

    @PostMapping(value = "/transactions")
    public ResponseEntity<Void> create(@Valid @NotNull @RequestBody TransactionRequest transactionRequest) {

        if (currentTimeMillis() - transactionRequest.getTimestamp() > TIME_LIMIT) {
            transactionService.createTransaction(transactionRequest.getAmount(), transactionRequest.getTimestamp());
            return new ResponseEntity<>(NO_CONTENT);
        } else {
            transactionService.createTransaction(transactionRequest.getAmount(), transactionRequest.getTimestamp());
            return new ResponseEntity<>(CREATED);
        }
    }

    @GetMapping(name = "/statistics", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionStatisticsResponse> getTransactionStatistics() {

        TransactionStatisticsDTO transactionStatisticsDTO = transactionService.getTransactionStatistics();
        TransactionStatisticsResponse response = new ModelMapper().map(transactionStatisticsDTO, TransactionStatisticsResponse.class);
        return new ResponseEntity<>(response, OK);
    }


}
