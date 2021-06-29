package com.bookstore.booksservice.service;

import com.bookstore.booksservice.dto.BookDTO;
import com.bookstore.booksservice.response.Response;

public interface IBookService {

	Response addBook(BookDTO bookDTO);

	Response updateBook(BookDTO bookDTO, String token , long bookId);

	Response viewBooks(String token);

	Response viewBook(String token ,long bookId);

	Response removeBook(String token ,long bookId);

	Response addBooksToInventory(long bookNumber, int quantity);

	Response changeBookPrice(long bookNumber, float price);
	
	String fetchBookTilte(long bookId);

	void changeBookQuantityAfterPurchase(long bookId, int quantity);

}
