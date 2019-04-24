package com.rentitem.restapi.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentitem.restapi.api.entity.ItemRent;

public interface ItemRentRepository extends JpaRepository<ItemRent, String> {

	ItemRent findOneById(String id);
	
	//TODO: Adicionar método que retorne itens a serem devolvidos no período
	
	//TODO: Adicionar método que retorne itens alugados no período
	
}
