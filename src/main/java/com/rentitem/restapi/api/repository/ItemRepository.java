package com.rentitem.restapi.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rentitem.restapi.api.entity.Item;

public interface ItemRepository extends JpaRepository<Item, String> {

	Item findOneById(String id);
	
	@Query("SELECT i FROM Item i JOIN ItemRent ir ON i.id = ir.item.id WHERE ir.dtReturn BETWEEN :startDate AND :finishDate")
	List<Item> findItemRentDevolutionPerPeriod(@Param("startDate") LocalDateTime startDate, @Param("finishDate") LocalDateTime finishDate);
	
	@Query("SELECT i FROM Item i JOIN ItemRent ir ON i.id = ir.item.id WHERE ir.dtRent BETWEEN :startDate AND :finishDate")
	List<Item> findItemRentPerPeriod(@Param("startDate") LocalDateTime startDate, @Param("finishDate") LocalDateTime finishDate);
}
