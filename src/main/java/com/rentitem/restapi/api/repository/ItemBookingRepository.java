package com.rentitem.restapi.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rentitem.restapi.api.entity.ItemBooking;

public interface ItemBookingRepository extends JpaRepository<ItemBooking, String> {

	ItemBooking findOneById(String id);
	
	@Query("SELECT ib FROM ItemBooking ib WHERE ib.item.id = :itemId")
	ItemBooking findByItemId(@Param("itemId") String itemId);
}
