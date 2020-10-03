package com.api.library.service.impl;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.api.library.dto.LoanFilterDto;
import com.api.library.exceptions.BusinessException;
import com.api.library.model.entity.Book;
import com.api.library.model.entity.Loan;
import com.api.library.model.repository.LoanRepository;
import com.api.library.service.LoanService;

@Service
public class LoanServiceImpl implements LoanService{
	
	private LoanRepository repository;
	
	public LoanServiceImpl(LoanRepository loanRepository) {
		this.repository = loanRepository;
	}

	@Override
	public Loan save(Loan loan) {
		if (repository.existsByBookAndNotReturned(loan.getBook())) {
			throw new BusinessException("Book already loaned");
		}
		return repository.save(loan);
	}

	@Override
	public Optional<Loan> getById(Long id) {
		return repository.findById(id);
	}

	@Override
	public Loan update(Loan loan) {
		return repository.save(loan);
	}

	@Override
	public Page<Loan> find(LoanFilterDto loan, Pageable pageRequest) {
		return repository.findByBookIsbnOrCustomer(loan.getIsbn(), loan.getCustomer(), pageRequest);
	}

	@Override
	public Page<Loan> getLoansByBook(Book book, Pageable pageable) {
		return repository.findByBook(book, pageable);
	}

}
