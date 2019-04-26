package com.rentitem.restapi.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rentitem.restapi.api.entity.ItemRent;

public interface ItemRentRepository extends JpaRepository<ItemRent, String> {

	ItemRent findOneById(String id);
	
	@Query("SELECT ir FROM ItemRent ir WHERE ir.item.id = :itemId")
	ItemRent findByItemId(@Param("itemId") String itemId);
	
	//TODO: Adicionar método que retorne itens a serem devolvidos no período
	
	//TODO: Adicionar método que retorne itens alugados no período
	
}
