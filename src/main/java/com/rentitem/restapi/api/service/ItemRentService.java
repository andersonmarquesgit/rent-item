package com.rentitem.restapi.api.service;

import org.springframework.data.domain.Page;

import com.rentitem.restapi.api.entity.ItemRent;

public interface ItemRentService {

	ItemRent createOrUpdate(ItemRent itemRent);
	
	ItemRent findById(String id);
	
	void delete(String id);
	
	Page<ItemRent> findAll(int page, int count);
	
}
