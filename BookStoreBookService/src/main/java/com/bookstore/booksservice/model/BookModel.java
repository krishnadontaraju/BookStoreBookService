package com.bookstore.booksservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.bookstore.booksservice.dto.BookDTO;

import lombok.Data;

@Entity
@Table(name = "books")
@Data
public class BookModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long bookId;
	
	private String title;
	private String description;
	private String author;
	private String logo;
	private String image;
	private float price;
	
	public void updatecustomerDetails(BookDTO bookDTO) {
		this.author = bookDTO.getAuthor();
		this.description = bookDTO.getDescription();
		this.logo = bookDTO.getLogo();
		this.price = bookDTO.getPrice();
		this.image = bookDTO.getImage();
		this.title = bookDTO.getTitle();
		
	}

}
