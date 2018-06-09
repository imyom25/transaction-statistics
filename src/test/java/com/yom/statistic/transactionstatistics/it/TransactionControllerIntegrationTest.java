package com.yom.statistic.transactionstatistics.it;

import com.yom.statistic.transactionstatistics.params.TransactionRequest;
import com.yom.statistic.transactionstatistics.params.TransactionStatisticsResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static com.yom.statistic.transactionstatistics.factory.TransactionObjectFactory.AMOUNT;
import static com.yom.statistic.transactionstatistics.factory.TransactionObjectFactory.TIMESTAMP;
import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class TransactionControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testShouldCreateTransactionAndReturn204_WhenTimestampIsOlderThan60Seconds() {

        TransactionRequest request = new TransactionRequest();
        request.setTimestamp(TIMESTAMP);
        request.setAmount(AMOUNT);

        ResponseEntity<Void> responseEntity =
                restTemplate.postForEntity("/transactions", request, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testShouldCreateTransactionAndReturn201_WhenTimestampIsNotOlderThan60Seconds() {

        TransactionRequest request = new TransactionRequest();
        request.setTimestamp(currentTimeMillis());
        request.setAmount(AMOUNT);

        ResponseEntity<Void> responseEntity =
                restTemplate.postForEntity("/transactions", request, Void.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void testShouldReturnStatistics_WhenThereIsNoTransactionWithin60Seconds() {

        ResponseEntity<TransactionStatisticsResponse> responseEntity =
                restTemplate.getForEntity("/statistics", TransactionStatisticsResponse.class);

        TransactionStatisticsResponse statisticsResponse = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        TransactionStatisticsResponse emptyResponse = new TransactionStatisticsResponse();
        assertEquals(emptyResponse, statisticsResponse);
    }

}
