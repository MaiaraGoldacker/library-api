package com.api.library.resource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.api.library.model.entity.Book;
import com.api.library.dto.BookDto;
import com.api.library.exceptions.BusinessException;
import com.api.library.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class) /*Spring cria contexto- injeção de dependência para rodar teste*/
@ActiveProfiles("test") /*Roda no contexto de test - roda apenas no ambiente de test*/

//configuram testes para rest api
@WebMvcTest
@AutoConfigureMockMvc/*Spring configura objeto para fazer as requisições do test*/
public class BookControllerTest {
	
	static String BOOK_API = "/api/books";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	BookService service;
	
	@Test
	@DisplayName("Deve criar um livro com sucesso.")
	public void createBookTest() throws Exception{
		
		BookDto dto = createNewBook();
		
		Book savedBook = Book.builder().id(101L)
									   .author("Artur")
									   .title("As Aventuras")
									   .isbn("001")
									   .build();
		
		BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook); //para gerar o ID do objeto
		String json = new ObjectMapper().writeValueAsString(dto); //transforma objeto em json
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
							  .contentType(MediaType.APPLICATION_JSON)
							  .accept(MediaType.APPLICATION_JSON)
							  .content(json);
		
		//verificadores
		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isCreated())  //espera status 201 - created
		.andExpect(jsonPath("id").isNotEmpty()) //espera o retorno do objeto criado - id não vazio
		.andExpect(jsonPath("title").value(dto.getTitle()))
		.andExpect(jsonPath("author").value(dto.getAuthor()))
		.andExpect(jsonPath("isbn").value(dto.getIsbn()))
		;
	}
	
	@Test
	@DisplayName("Deve lançar erro quando não houver dados o suficiente para criação do livro.")
	public void createInvalidBookTest() throws Exception{
		String json = new ObjectMapper().writeValueAsString(new BookDto()); 
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
				  .contentType(MediaType.APPLICATION_JSON)
				  .accept(MediaType.APPLICATION_JSON)
				  .content(json);
		
		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(jsonPath("errors", Matchers.hasSize(3))); //pois são 3 propriedades obrigatórias que não estarão preenchidas
	}
	
	@Test
	@DisplayName("Deve lançar erro ao testar cadastrar livro com ISBN duplicado")
	public void createBookWithDuplicatedIsbn() throws Exception {
		BookDto dto = createNewBook();
		
		String json = new ObjectMapper().writeValueAsString(dto); 
		BDDMockito.given(service.save(Mockito.any()))
			.willThrow(new BusinessException("Isbn já Cadastrado"));
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
				  .contentType(MediaType.APPLICATION_JSON)
				  .accept(MediaType.APPLICATION_JSON)
				  .content(json);
	}
	
	public BookDto createNewBook() {
		return BookDto.builder().author("Artur")
					  .title("As Aventuras")
					  .isbn("001")
					  .build();
	}
}
