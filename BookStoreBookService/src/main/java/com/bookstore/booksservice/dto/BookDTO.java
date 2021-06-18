package com.bookstore.booksservice.dto;

import lombok.Data;

@Data
public class BookDTO {
	
	private String title;
	private String description;
	private String author;
	private String logo;
	private String image;
	private float price;
	private int quantity;
	
}
