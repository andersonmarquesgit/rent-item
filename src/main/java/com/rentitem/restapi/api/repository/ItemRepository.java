package com.rentitem.restapi.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentitem.restapi.api.entity.Item;

public interface ItemRepository extends JpaRepository<Item, String> {

	Item findOneById(String id);
	
}
