package com.bookstore.booksservice.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Response addBook(BookDTO bookDTO) {
		log.info("Book Creation Accessed");
		
		Optional<BookModel> doesBookExist = bookRepository.findByTitle(bookDTO.getTitle());
		//Checking if duplicates exist if not then proceeding
		if (doesBookExist.isPresent()) {
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
		}else {
			log.info("No duplicates found Initiating book Creation");
			BookModel book = mapper.map(bookDTO , BookModel.class);
			log.info("Book Successfully created"+book);
			bookRepository.save(book);	
			return new Response("Successfully added Book" ,book);
		}
	}

	//Updating book with a dto
	@Override
	public Response updateBook(BookDTO bookDTO, String token , long bookId) {
		log.info("Accessed Book Updation");
		long customerId = tokenManager.decodeToken(token);
		
		boolean doesCustomerExist = restTemplate.getForObject("http:/customerService/customer/checkCustomer/"+customerId , boolean.class);
		
		if (doesCustomerExist == true) {
			Optional<BookModel> doesBookExist = bookRepository.findById(bookId);
			
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
		}else {
			log.error("Customer could not verified");
			throw new BookServiceException(601,"Customer could not be verified ");
		}

		
	}

	//Viewing all books 
	@Override
	public Response viewBooks(String token) {
		log.info("Accessed all Books retrieval");
		long customerId = tokenManager.decodeToken(token);
		boolean doesCustomerExist = restTemplate.getForObject("http:/customerService/customer/checkCustomer/"+customerId , boolean.class);
		
		if (doesCustomerExist == true) {
			log.info("token has been verified now retrieving all Books");
			return new Response("Books retrieval complete" ,bookRepository.findAll());
		}else {
			log.error("Could not verify token "+token);
			throw new BookServiceException(501 , "Token given for all books retrieval was incorrect");
		}
	}

	//Viewing a specific book
	@Override
	public Response viewBook(String token ,long bookId ) {
		log.info("Accessed A Book retrieval");
		long customerId = tokenManager.decodeToken(token);
		boolean doesCustomerExist = restTemplate.getForObject("http:/customerService/customer/checkCustomer/"+customerId , boolean.class);
		Optional<BookModel> doesBookExist = bookRepository.findById(bookId);
		
		if (doesCustomerExist == true) {
			if (doesBookExist.isPresent()) {
				log.info("Book with Title"+doesBookExist.get().getTitle()+" written by "+
						doesBookExist.get().getAuthor()+"has been found now retrieving Book");
				return new Response("Book retirieved" ,doesBookExist.get());
			}else {
				log.error("Book was not found with token "+token);
				throw new BookServiceException(501 , "Book not found with given details");
			}
		}else {
			log.error("Customer could not verified");
			throw new BookServiceException(601,"Customer could not be verified ");
		}
		
		
	}

	//Removing a book From Inventory
	@Override
	public Response removeBook(String token ,long bookId) {
		log.info("Accessed Book removal");
		long customerId = tokenManager.decodeToken(token);
		boolean doesCustomerExist = restTemplate.getForObject("http:/customerService/customer/checkCustomer/"+customerId , boolean.class);
		Optional<BookModel> doesBookExist = bookRepository.findById(bookId);
		
		if (doesCustomerExist == true) {
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
		}else {
			log.error("Customer could not verified");
			throw new BookServiceException(601,"Customer could not be verified ");
		}
		
	}

	//Adding Books to the Inventory
	@Override
	public Response addBooksToInventory(long bookNumber, int quantity) {
		log.info("Accessed change book available quantity");
		Optional<BookModel> doesBookExist = bookRepository.findById(bookNumber);
		
		if (doesBookExist.isPresent()) {
			log.info("Book with Title "+doesBookExist.get().getTitle()+" has been found now, changing available quantity");
			doesBookExist.get().setQuantity(doesBookExist.get().getQuantity() + quantity);
			bookRepository.save(doesBookExist.get());
			log.info("Book quantity has been reduced to "+doesBookExist.get().getQuantity()+" After adding books to Inventory");
			return new Response("Successfully updated the customer details" ,doesBookExist.get());
		}else {
			log.error("Book was not found for updation with Serial Number "+bookNumber);
			throw new BookServiceException(501 , "Book not found with given details");
		}
	}

	//changing book price
	@Override
	public Response changeBookPrice(long bookNumber, float price) {
		
		log.info("Accessed change book price");
		
		Optional<BookModel> doesBookExist = bookRepository.findById(bookNumber);
		
		if (doesBookExist.isPresent()) {
			log.info("Book with Title "+doesBookExist.get().getTitle()+" has been found now, changing book price");
			doesBookExist.get().setPrice(price);
			bookRepository.save(doesBookExist.get());
			log.info("Book price has been updated to "+doesBookExist.get().getPrice());
			return new Response("Successfully updated the customer details" ,doesBookExist.get());
		}else {
			log.error("Book was not found for updation with Serial Number "+bookNumber);
			throw new BookServiceException(501 , "Book not found with given details");
		}
	}

	//This method is for Order Service to fetch the title to email Customer his Order
	@Override
	public String fetchBookTilte(long bookId) {
		log.info("Order service has requested the Tilte");
		Optional<BookModel> doesBookExist = bookRepository.findById(bookId);
		
		if (doesBookExist.isPresent()) {
			return doesBookExist.get().getTitle();
		}
		log.error("Title could not be found with ID "+bookId);
		throw new BookServiceException(501 , "Book could not be found");
	}

	//This method is responsible for managing the quantity of books after a purchase is made
	@Override
	public void changeBookQuantityAfterPurchase(long bookId, int quantity) {
		log.info("Accessed change book available quantity after Purchase");
		Optional<BookModel> doesBookExist = bookRepository.findById(bookId);
		
		if (doesBookExist.isPresent()) {
			log.info("Book with Title "+doesBookExist.get().getTitle()+" has been found now, changing available quantity");
			doesBookExist.get().setQuantity(doesBookExist.get().getQuantity() - quantity);
			bookRepository.save(doesBookExist.get());
			log.info("Book quantity has been reduced to "+doesBookExist.get().getQuantity()+" After purchase");
		}else {
			log.error("Book was not found for updation after purchase with Serial Number "+bookId);
			throw new BookServiceException(501 , "Book not found with given details");
		}
		
	}
	
}
