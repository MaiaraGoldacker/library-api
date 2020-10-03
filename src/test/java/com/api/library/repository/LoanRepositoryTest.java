package com.api.library.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.library.model.entity.Book;
import com.api.library.model.entity.Loan;
import com.api.library.model.repository.LoanRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

	@Autowired
	TestEntityManager entityManager;
	
	@Autowired
	LoanRepository repository;
	
	@Test
	@DisplayName("Deve verificar se existe empréstimo não devolvido para o livro")
	public void existsByBookAndNotReturned(){
		Loan loan = createAndPersistLoan();
		Book book = loan.getBook();
		
		//execução
		Boolean exists = repository.existsByBookAndNotReturned(book);
		
		assertThat(exists).isTrue();
	}
	
	@Test
	@DisplayName("Deve buscar empréstimo pelo isbn do livro ou customer")
	public void findByBookIsbnOrCustomer() {
		
		Loan loan = createAndPersistLoan();	
		Page<Loan> result = repository.findByBookIsbnOrCustomer("123", "Fulano", PageRequest.of(0,10));
		
		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent()).contains(loan);
		assertThat(result.getPageable().getPageSize()).isEqualTo(10);
		assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
		assertThat(result.getTotalElements()).isEqualTo(1);
	}
	
	public Loan createAndPersistLoan() {
		//cenario
		Book book = Book.builder().isbn("123").title("As aventuras").author("Fulano").build();
		entityManager.persist(book);
						
		Loan loan = Loan.builder().customer("Fulano").book(book).loanDate(LocalDate.now()).build();
		entityManager.persist(loan);
		
		return loan;
	}
}
