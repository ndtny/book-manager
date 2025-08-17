package com.david.bookmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BookmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookmanagerApplication.class, args);
	}

}
