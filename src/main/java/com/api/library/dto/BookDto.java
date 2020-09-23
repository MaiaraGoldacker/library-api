package com.api.library.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
public class BookDto {
	
	private Long id;
	private String title;
	private String author;
	private String isbn;

}
