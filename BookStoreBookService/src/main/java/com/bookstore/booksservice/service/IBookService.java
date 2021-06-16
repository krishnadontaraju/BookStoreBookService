package com.bookstore.booksservice.service;

import com.bookstore.booksservice.dto.BookDTO;
import com.bookstore.booksservice.response.Response;

public interface IBookService {

	Response addBook(BookDTO bookDTO);

	Response updateBook(BookDTO bookDTO, String token);

	Response viewBooks(String token);

	Response viewBook(String token);

	Response removeBook(String token);

}
