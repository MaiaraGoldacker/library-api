package com.api.library.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class BookDto {
	
	public BookDto() {
		
	}
	
	private Long id;
	private String title;
	private String author;
	private String isbn;

}
