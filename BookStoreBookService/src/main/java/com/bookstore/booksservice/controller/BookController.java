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
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@PutMapping("/updateBook/{bookId}")
	public ResponseEntity<Response> updateBook(@RequestBody BookDTO bookDTO , @RequestHeader String token ,@PathVariable long bookId) {
		
		Response updateBookResponse = bookService.updateBook(bookDTO , token ,bookId);
		return new ResponseEntity<Response> (updateBookResponse , HttpStatus.OK);
		
	}
	
	//End point to view all books
	
	@GetMapping(value = {"" ,"/" , "/viewBooks"})
	public ResponseEntity<Response> viewBooks(@RequestHeader String token) {
		
		Response viewBooksResponse = bookService.viewBooks(token);
		return new ResponseEntity<Response> (viewBooksResponse , HttpStatus.OK);
		
	}
	
	//End point to view a particular book
	
	@GetMapping("/viewBook/{bookId}")
	public ResponseEntity<Response> viewBook(@PathVariable long bookId , @RequestHeader String token) {
		
		Response viewBookResponse = bookService.viewBook(token , bookId);
		return new ResponseEntity<Response> (viewBookResponse , HttpStatus.OK);
		
	}
	
	//End point to remove the book
	
	@DeleteMapping("/removeBook/{bookId}")
	public ResponseEntity<Response> removeBook(@RequestHeader String token ,@PathVariable long bookId) {
		
		Response removeBookResponse = bookService.removeBook(token , bookId);
		return new ResponseEntity<Response> (removeBookResponse , HttpStatus.OK);
		
	}
	
	//End point to change quantity of the Book available
	@PutMapping("/changeBookQunatity/{bookNumber}")
	public ResponseEntity<Response> changeBookQuantityAvailable (@PathVariable long bookNumber , int quantity){
		
		Response chnageBookQuantityResponse = bookService.addBooksToInventory(bookNumber , quantity);
		return new ResponseEntity<Response> (chnageBookQuantityResponse , HttpStatus.OK);
		
	}
	
	//End point to Book price
	@PutMapping("/changeBookPrice/{bookNumber}")
	public ResponseEntity<Response> changeBookPrice(@PathVariable long bookNumber , float price){
		
		Response changeBookPriceResponse = bookService.changeBookPrice(bookNumber , price);
		return new ResponseEntity<Response> (changeBookPriceResponse , HttpStatus.OK);
		
	}
	
	//End point for order service to fetch the book
	@GetMapping("/fetchTitle/{bookId}")
	public String fetchTitle(@PathVariable long bookId){
		
		return bookService.fetchBookTilte(bookId);
		
	}
	
	//End Point for Order Service to update the quantity of the books after order purchase
	@PutMapping("/changeQuantity/{bookId}")
	public void changeBookQuantityAfterPurchase(@PathVariable("bookId") long bookId , @RequestParam int quantity){
		
		bookService.changeBookQuantityAfterPurchase(bookId , quantity);
	}
}
