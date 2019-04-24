package com.rentitem.restapi.api.service;

import org.springframework.data.domain.Page;

import com.rentitem.restapi.api.entity.ItemType;

public interface ItemTypeService {

	ItemType createOrUpdate(ItemType itemType);
	
	ItemType findById(String id);
	
	void delete(String id);
	
	Page<ItemType> findAll(int page, int count);
	
}
