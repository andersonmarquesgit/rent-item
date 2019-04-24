package com.rentitem.restapi.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentitem.restapi.api.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {

	Customer findByEmail(String email);
	
	Customer findOneById(String id);
	
}
