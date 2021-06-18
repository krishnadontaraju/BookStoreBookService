package com.bookstore.booksservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.booksservice.dto.BookDTO;
import com.bookstore.booksservice.response.Response;
import com.bookstore.booksservice.service.IBookService;

@RestController
@RequestMapping("/book")
public class BookController {

	@Autowired
	private IBookService bookService;
	
	//End point to add a book
	
	@PostMapping("/addBook")
	public ResponseEntity<Response> addBook(@RequestBody BookDTO bookDTO) {
		
		Response addBookResponse = bookService.addBook(bookDTO);
		return new ResponseEntity<Response> (addBookResponse , HttpStatus.CREATED);
		
	}
	
	//End pint to update the book details
	
	@PutMapping("/updateBook")
	public ResponseEntity<Response> updateBook(@RequestBody BookDTO bookDTO , @RequestHeader String token) {
		
		Response updateBookResponse = bookService.updateBook(bookDTO , token);
		return new ResponseEntity<Response> (updateBookResponse , HttpStatus.OK);
		
	}
	
	//End point to view all books
	
	@GetMapping(value = {"" ,"/" , "/viewBooks"})
	public ResponseEntity<Response> viewBooks(@RequestHeader String token) {
		
		Response viewBooksResponse = bookService.viewBooks(token);
		return new ResponseEntity<Response> (viewBooksResponse , HttpStatus.OK);
		
	}
	
	//End point to view a particular book
	
	@GetMapping("/viewBook/{token}")
	public ResponseEntity<Response> viewBook(@PathVariable String token) {
		
		Response viewBookResponse = bookService.viewBook(token);
		return new ResponseEntity<Response> (viewBookResponse , HttpStatus.OK);
		
	}
	
	//End point to remove the book
	
	@DeleteMapping("/removeBook")
	public ResponseEntity<Response> removeBook(@RequestHeader String token) {
		
		Response removeBookResponse = bookService.removeBook(token);
		return new ResponseEntity<Response> (removeBookResponse , HttpStatus.OK);
		
	}
	
	//End point to change quantity of the Book available
	@PutMapping("/changeBookQunatity/{bookNumber}")
	public ResponseEntity<Response> changeBookQuantityAvailable (@PathVariable long bookNumber , int quantity){
		
		Response chnageBookQuantityResponse = bookService.changeBookQuantity(bookNumber , quantity);
		return new ResponseEntity<Response> (chnageBookQuantityResponse , HttpStatus.OK);
		
	}
	
	//End point to Book price
	@PutMapping("/changeBookPrice/{bookNumber}")
	public ResponseEntity<Response> changeBookPrice(@PathVariable long bookNumber , float price){
		
		Response changeBookPriceResponse = bookService.changeBookPrice(bookNumber , price);
		return new ResponseEntity<Response> (changeBookPriceResponse , HttpStatus.OK);
		
	}
	
}
