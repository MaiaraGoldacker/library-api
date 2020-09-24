package com.api.library.service.impl;

import org.springframework.stereotype.Service;

import com.api.library.model.entity.Book;
import com.api.library.model.repository.BookRepository;
import com.api.library.service.BookService;

@Service
public class BookServiceImpl implements BookService{
	
	private BookRepository repository;
	
	public BookServiceImpl(BookRepository bookRepository) {
		this.repository = bookRepository;
	}
	
	@Override
	public Book save(Book book) {
		Book savedBook = repository.save(book);
		return savedBook;
	}

}
