package com.rentitem.restapi.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentitem.restapi.api.entity.ItemBooking;

public interface ItemBookingRepository extends JpaRepository<ItemBooking, String> {

	ItemBooking findOneById(String id);
	
}
