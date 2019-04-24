package com.rentitem.restapi.api.service;

import org.springframework.data.domain.Page;

import com.rentitem.restapi.api.entity.Customer;

public interface CustomerService {

	Customer findByEmail(String email);
	
	Customer createOrUpdate(Customer customer);
	
	Customer findById(String id);
	
	void delete(String id);
	
	Page<Customer> findAll(int page, int count);
	
}
