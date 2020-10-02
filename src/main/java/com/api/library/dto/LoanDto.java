package com.api.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LoanDto {
	
	public LoanDto() {
		
	}
	
	private Long id;
	
	private String isbn;
	
	private String customer;
	
	private BookDto book;
}
