package com.api.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.api.library.dto.LoanFilterDto;
import com.api.library.exceptions.BusinessException;
import com.api.library.model.entity.Book;
import com.api.library.model.entity.Loan;
import com.api.library.model.repository.LoanRepository;
import com.api.library.service.impl.LoanServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {
	
	@MockBean
	private LoanRepository loanRepository;
	
	@MockBean
	private LoanService loanService;
	
	@BeforeEach //executado antes de cada teste
	public void setUp() {
		this.loanService = new LoanServiceImpl(loanRepository);
	}
	
	@Test
	@DisplayName("Deve salvar um empréstimo")
	public void saveLoanTest() {
		Book book = Book.builder().id(1L).build();
		String customer = "Fulano";
		
		Loan savingLoan = Loan.builder().book(book)
								  .customer(customer)
								  .loanDate(LocalDate.now())
								  .build();
		
		Loan savedLoan = Loan.builder().book(book)
									   .id(1L)
				  					   .customer(customer)
				  					   .loanDate(LocalDate.now())
				  					   .build();
		when(loanRepository.existsByBookAndNotReturned(book)).thenReturn(false); 
		when(loanRepository.save(savingLoan)).thenReturn(savedLoan);
		
		Loan loan = loanService.save(savingLoan);
		
		assertThat(loan.getId()).isEqualTo(savedLoan.getId());
		assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
		assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
		assertThat(loan.getBook()).isEqualTo(savedLoan.getBook());
	}
	
	@Test
	@DisplayName("Deve lançar erro de negócio ao salvar um empréstimo com livro já emprestado")
	public void loanedBookSaveTest() {
		Book book = Book.builder().id(1L).build();
		String customer = "Fulano";
		
		Loan savingLoan = Loan.builder().book(book)
								  .customer(customer)
								  .loanDate(LocalDate.now())
								  .build();
		
		when(loanRepository.existsByBookAndNotReturned(book)).thenReturn(true); //sempre vai retornar verdadeiro
		
		Throwable exception = catchThrowable(() -> loanService.save(savingLoan));
		assertThat(exception).isInstanceOf(BusinessException.class).hasMessage("Book already loaned");
	}
	
	
	@Test
	@DisplayName("Deve obter as informações de um empréstimo pelo ID")
	public void getLoanDetailsTest(){
		Long id = 1L;
		
		Loan loan = createLoan();
		loan.setId(id);
		
		Mockito.when(loanRepository.findById(id)).thenReturn(Optional.of(loan));
		
		Optional<Loan> result = loanService.getById(id);
		
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getId()).isEqualTo(id);
		assertThat(result.get().getCustomer()).isEqualTo(loan.getCustomer());
		assertThat(result.get().getLoanDate()).isEqualTo(loan.getLoanDate());
		assertThat(result.get().getBook()).isEqualTo(loan.getBook());
		
		verify(loanRepository).findById(id);
	}
	
	
	@Test
	@DisplayName("Deve atualizar um empréstimo")
	public void updateLoanTest() {
		Loan loan = createLoan();
		loan.setId(1L);
		loan.setReturned(true);
		
		when( loanRepository.save(loan)).thenReturn(loan);
		
		Loan updatedLoan = loanService.update(loan);
		assertThat(updatedLoan.getReturned()).isTrue();
		verify(loanRepository).save(loan);
		
	}
	
	@Test
	@DisplayName("Deve filtrar empréstimos pelas propriedades")
	public void findLoanTest() {
		
		LoanFilterDto loanFilterDto = LoanFilterDto.builder().customer("Fulano").isbn("321").build();
 		//cenario
		Loan loan = createLoan();
		loan.setId(1L);
		
		PageRequest pageRequest = PageRequest.of(0, 10);
		
		List<Loan> lista = Arrays.asList(loan);
		Page<Loan> page = new PageImpl<Loan>(lista, pageRequest, lista.size());
		
		Mockito.when(loanRepository.findByBookIsbnOrCustomer(
							Mockito.anyString(), 
							Mockito.anyString(),
							Mockito.any(PageRequest.class)))
								   .thenReturn(page);
		//execução
		Page<Loan> result = loanService.find(loanFilterDto, pageRequest);
		
		//verificações
		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getContent()).isEqualTo(lista);
		assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
		assertThat(result.getPageable().getPageSize()).isEqualTo(10);
	}
	
	
	public static Loan createLoan() {
		Book book = Book.builder().id(1L).build();
		String customer = "Fulano";
		
		return Loan.builder().book(book)
							 .customer(customer)
							 .loanDate(LocalDate.now())
							 .build();
	}

}
