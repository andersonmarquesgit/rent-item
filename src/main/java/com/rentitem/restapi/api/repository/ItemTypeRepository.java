package com.rentitem.restapi.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentitem.restapi.api.entity.ItemType;

public interface ItemTypeRepository extends JpaRepository<ItemType, String> {

	ItemType findOneById(String id);
	
}
