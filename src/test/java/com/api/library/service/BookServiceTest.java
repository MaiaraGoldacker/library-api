package com.api.library.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.api.library.exceptions.BusinessException;
import com.api.library.model.entity.Book;
import com.api.library.model.repository.BookRepository;
import com.api.library.service.impl.BookServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

	BookService service;
	
	@MockBean
	BookRepository repository;
	
	@BeforeEach //executado antes de cada teste
	public void setUp() {
		this.service = new BookServiceImpl(repository);
	}
	
	@Test
	@DisplayName("Deve salvar o livro.")
	public void saveBookTest() {
		//cenario
		Book book = Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
		Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
		
		Mockito.when(repository.save(book))
			.thenReturn(Book.builder().id(11L)
									  .isbn("123")
									  .title("As aventuras")
									  .author("Fulano")
									  .build());
		//execução
		Book savedBook = service.save(book);
		
		//verificação
		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getIsbn()).isEqualTo("123");
		assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
		assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
	}
	
	@Test
	@DisplayName("Deve lançar erro de negócio ao tentar salvar livro com isbn duplicado")
	public void shouldNotSaveABookWithDuplicatedISBN() {
		Book book = createValidBook();
		
		//simula que sempre vai dar verdadeiro, a condição de existsByIsbn
		Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);
		
		//assertions = assertj.core.api
		Throwable exception = Assertions.catchThrowable(() ->service.save(book));
		assertThat(exception).isInstanceOf(BusinessException.class)
							 .hasMessage("Isbn ja Cadastrado");
		
		//verifica que método nunca será executado com esse parâmetro
		Mockito.verify(repository, Mockito.never()).save(book);
	}
	
	public Book createValidBook() {
		return Book.builder().author("Artur")
				   .title("As Aventuras")
				   .isbn("001")
				   .build();
	}
}
