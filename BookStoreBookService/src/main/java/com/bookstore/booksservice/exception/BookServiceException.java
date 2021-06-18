package com.bookstore.booksservice.exception;

@SuppressWarnings("serial")
public class BookServiceException extends RuntimeException {

	@SuppressWarnings("unused")
	private int exceptionCode;
	@SuppressWarnings("unused")
	private String message;
	
	public BookServiceException(int exceptionCode ,String message) {
		super(message);
		this.exceptionCode = exceptionCode;
	}

}

