package com.api.library.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.api.library.exceptions.BusinessException;
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
		if (repository.existsByIsbn(book.getIsbn())) {
			throw new BusinessException("Isbn ja Cadastrado");
		}
		
		Book savedBook = repository.save(book);
		return savedBook;
	}

	@Override
	public Optional<Book> getById(Long id) {
		return null;
	}

	@Override
	public void delete(Book book) {
		// TODO Auto-generated method stub	
	}

	@Override
	public Book update(Book book) {
		return null;
		// TODO Auto-generated method stub
	}

}
