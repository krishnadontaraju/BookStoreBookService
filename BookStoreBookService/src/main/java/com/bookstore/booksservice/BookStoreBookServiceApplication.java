package com.bookstore.booksservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BookStoreBookServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookStoreBookServiceApplication.class, args);
	}

}
