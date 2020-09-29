package com.api.library.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
		return this.repository.findById(id);
	}

	@Override
	public void delete(Book book) {
		if (book == null || book.getId() == null) {
			throw new IllegalArgumentException("book id cant be null");
		}
		this.repository.delete(book);
	}

	@Override
	public Book update(Book book) {
		if (book == null || book.getId() == null) {
			throw new IllegalArgumentException("book id cant be null");
		}
		return this.repository.save(book);
	}

	@Override
	public Page<Book> find(Book filter, Pageable pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}
