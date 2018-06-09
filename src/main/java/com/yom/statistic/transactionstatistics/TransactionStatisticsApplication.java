package com.yom.statistic.transactionstatistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;

@EnableCaching
@SpringBootApplication
public class TransactionStatisticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionStatisticsApplication.class, args);
	}
}





