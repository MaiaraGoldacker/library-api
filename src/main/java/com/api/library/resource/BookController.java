package com.api.library.resource;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.library.dto.BookDto;
import com.api.library.exceptions.ApiErrors;
import com.api.library.exceptions.BusinessException;
import com.api.library.model.entity.Book;
import com.api.library.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {

	private BookService service;
	
	private ModelMapper modelMapper;
	
	public BookController(BookService service, ModelMapper modelMapper) {
		this.service = service;
		this.modelMapper = modelMapper;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookDto create(@RequestBody @Valid BookDto dto) {
		
		Book entity = modelMapper.map(dto, Book.class);
		entity = service.save(entity);
		return modelMapper.map(entity, BookDto.class);
		
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult(); 
		return new ApiErrors(bindingResult);
	}
	
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleBusinessException(BusinessException ex) {
		return new ApiErrors(ex);
	}
	
	@GetMapping("{id}")
	public BookDto get(@PathVariable Long id){
		return service.getById(id)
					  .map(book -> modelMapper.map(book, BookDto.class))
					  .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)); //exception do spring boot que retorna o status http escolhido
		
	
	}
}
