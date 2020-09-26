package com.api.library.service;

import java.util.Optional;

import com.api.library.model.entity.Book;

public interface BookService {

	Book save(Book book);
	
	Optional<Book> getById(Long id);

}
