package com.bookstore.booksservice.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bookstore.booksservice.dto.BookDTO;
import com.bookstore.booksservice.exception.BookServiceException;
import com.bookstore.booksservice.model.BookModel;
import com.bookstore.booksservice.repository.BookRepository;
import com.bookstore.booksservice.response.Response;
import com.bookstore.booksservice.utility.TokenUtility;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookService implements IBookService{
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private TokenUtility tokenManager;

	@Override
	public Response addBook(BookDTO bookDTO) {
		log.info("Book Creation Accessed");
		
		Optional<BookModel> doesBookExist = bookRepository.findByTitle(bookDTO.getTitle());
		//Checking if duplicates exist if not then proceeding
		if (!(doesBookExist.get().getAuthor() == bookDTO.getAuthor() && doesBookExist.get().getTitle() == bookDTO.getTitle())) {
			log.info("No duplicates found Initiating book Creation");
			BookModel book = mapper.map(bookDTO , BookModel.class);
			log.info("Book Successfully created"+book);
			bookRepository.save(book);	
			return new Response("Successfully added Book" ,book);
			
		}else {
			log.error("Duplicate has been found with Tiltle "+bookDTO.getTitle()+" And Authored by "+bookDTO.getAuthor());
			throw new BookServiceException(501 , "Book with similar credentials already exists");
		}
	}

	@Override
	public Response updateBook(BookDTO bookDTO, String token) {
		log.info("Accessed Book Updation");
		long id = tokenManager.decodeToken(token);
		Optional<BookModel> doesBookExist = bookRepository.findById(id);
		
		if (doesBookExist.isPresent()) {
			log.info("Book with Title "+doesBookExist.get().getTitle()+" has been found now initiating updation procedure");
			doesBookExist.get().updatecustomerDetails(bookDTO);
			bookRepository.save(doesBookExist.get());
			log.info("Book Updation completed");
			return new Response("Successfully updated the customer details" ,doesBookExist.get());
		}else {
			log.error("Book was not found for updation with Title "+bookDTO.getTitle());
			throw new BookServiceException(501 , "Book not found with given details");
		}
	}

	@Override
	public Response viewBooks(String token) {
		log.info("Accessed all Books retrieval");
		long id = tokenManager.decodeToken(token);
		Optional<BookModel> doesBookExist = bookRepository.findById(id);
		
		if (doesBookExist.isPresent()) {
			log.info("token has been verified now retrieving all Books");
			return new Response("Books retrieval complete" ,bookRepository.findAll());
		}else {
			log.error("Could not verify token "+token);
			throw new BookServiceException(501 , "Token given for all books retrieval was incorrect");
		}
	}

	@Override
	public Response viewBook(String token) {
		log.info("Accessed A Book retrieval");
		long id = tokenManager.decodeToken(token);
		Optional<BookModel> doesBookExist = bookRepository.findById(id);
		
		if (doesBookExist.isPresent()) {
			log.info("Book with Title"+doesBookExist.get().getTitle()+" written by "+
					doesBookExist.get().getAuthor()+"has been found now retrieving Book");
			return new Response("Book retirieved" ,doesBookExist.get());
		}else {
			log.error("Book was not found with token "+token);
			throw new BookServiceException(501 , "Book not found with given details");
		}
	}

	@Override
	public Response removeBook(String token) {
		log.info("Accessed Book removal");
		long id = tokenManager.decodeToken(token);
		Optional<BookModel> doesBookExist = bookRepository.findById(id);
		
		if (doesBookExist.isPresent()) {
			log.info("token has been verified now removing Book "+doesBookExist.get().getTitle()+" written by "+
					doesBookExist.get().getAuthor());
			bookRepository.delete(doesBookExist.get());
			log.info("Book Removed");
			return new Response("Book removed" ,HttpStatus.FOUND);
		}else {
			log.error("Book was not found for removing with token "+token);
			throw new BookServiceException(501 , "Book not found with given details");
		}
	}

	
	
}
