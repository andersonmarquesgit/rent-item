package com.rentitem.restapi.api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.rentitem.restapi.api.entity.Item;

public interface ItemService {

	Item createOrUpdate(Item item);
	
	Item findById(String id);
	
	void delete(String id);
	
	Page<Item> findAll(int page, int count);
	
	List<Item> findItemRentDevolutionPerPeriod();
}
