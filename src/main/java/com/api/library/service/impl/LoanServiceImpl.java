package com.api.library.service.impl;

import org.springframework.stereotype.Service;

import com.api.library.exceptions.BusinessException;
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

}