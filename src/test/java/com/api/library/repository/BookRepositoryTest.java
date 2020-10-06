package com.api.library.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.library.model.entity.Book;
import com.api.library.model.repository.BookRepository;

/*Teste de integração*/
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest /*Indica que vai fazer teste de jpa, criando BD em memória apenas em tempo de execução*/
public class BookRepositoryTest {
	
	@Autowired
	TestEntityManager entityManager;
	
	@Autowired
	BookRepository bookRepository;
	
	@Test
	@DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado")
	public void returnTrueWhenIsbnExists() {
		//cenario
		String isbn = "123";
		
		Book book = createNewBook(isbn);
		
		entityManager.persist(book);
		
		//execução
		boolean exists = bookRepository.existsByIsbn(isbn);
		
		//validação
		assertThat(exists).isTrue();
		
	}
	
	@Test
	@DisplayName("Deve retornar false quando não existir um livro na base com o isbn informado")
	public void returnFalseWhenIsbnDoesntExists() {
		//cenario
		String isbn = "123";
		
		//execução
		boolean exists = bookRepository.existsByIsbn(isbn);
		
		//validação
		assertThat(exists).isFalse();
		
	}
	
	@Test
	@DisplayName("Deve obter um livro por Id")
	public void findByIdTest() {
		//cenario
		Book book = createNewBook("123");
		entityManager.persist(book);
		
		//execução 
		Optional<Book> foundBook =	bookRepository.findById(book.getId());
	
		//Validação
		assertThat(foundBook.isPresent()).isTrue();
	}
	
	@Test
	@DisplayName("Deve salvar um livro")
	public void saveBookTest() {
		
		Book book = createNewBook("123");
		Book savedBook = bookRepository.save(book);
		
		assertThat(savedBook.getId()).isNotNull();
	}

	@Test
	@DisplayName("Deve deletar um livro")
	public void deleteBookTest(){
		Book book = createNewBook("123");
		
		entityManager.persist(book);
		
		Book foundBook = entityManager.find(Book.class, book.getId());
		bookRepository.delete(foundBook);
		
		Book deletedBook = entityManager.find(Book.class, book.getId());
		assertThat(deletedBook).isNull();
	}
	
	private Book createNewBook(String isbn) {
		return Book.builder().isbn(isbn).title("As aventuras").author("Fulano").build();
	}
	
}
