package com.rentitem.restapi.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.rentitem.restapi.api.entity.Customer;
import com.rentitem.restapi.api.repository.CustomerRepository;
import com.rentitem.restapi.api.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public Customer findByEmail(String email) {
		return this.customerRepository.findByEmail(email);
	}

	@Override
	public Customer createOrUpdate(Customer customer) {
		return this.customerRepository.save(customer);
	}

	@Override
	public Customer findById(String id) {
		return this.customerRepository.findOneById(id);
	}

	@Override
	public void delete(String id) {
		this.customerRepository.deleteById(id);
	}

	@Override
	public Page<Customer> findAll(int page, int count) {
		return this.customerRepository.findAll(PageRequest.of(page, count));
	}

}
