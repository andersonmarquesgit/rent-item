package com.rentitem.restapi.api.service;

import org.springframework.data.domain.Page;

import com.rentitem.restapi.api.entity.ItemBooking;

public interface ItemBookingService {

	ItemBooking createOrUpdate(ItemBooking itemBooking);
	
	ItemBooking findById(String id);
	
	void delete(String id);
	
	Page<ItemBooking> findAll(int page, int count);
	
}
