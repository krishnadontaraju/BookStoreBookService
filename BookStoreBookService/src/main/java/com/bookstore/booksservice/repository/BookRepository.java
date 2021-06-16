package com.bookstore.booksservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.booksservice.model.BookModel;

public interface BookRepository extends JpaRepository<BookModel , Long>{

	Optional<BookModel> findByTitle(String title);

}
