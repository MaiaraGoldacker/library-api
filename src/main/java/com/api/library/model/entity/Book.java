package com.api.library.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Book {
	
	public Book() {
		
	}

	private Long id;
	private String title;
	private String author;
	private String isbn;

}
