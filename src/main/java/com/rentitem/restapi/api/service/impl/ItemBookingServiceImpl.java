package com.rentitem.restapi.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.rentitem.restapi.api.entity.ItemBooking;
import com.rentitem.restapi.api.repository.ItemBookingRepository;
import com.rentitem.restapi.api.service.ItemBookingService;

@Service
public class ItemBookingServiceImpl implements ItemBookingService {

	@Autowired
	private ItemBookingRepository itemBookingRepository;
	
	@Override
	public ItemBooking createOrUpdate(ItemBooking itemBooking) {
		return this.itemBookingRepository.save(itemBooking);
	}

	@Override
	public ItemBooking findById(String id) {
		return this.itemBookingRepository.findOneById(id);
	}

	@Override
	public void delete(String id) {
		this.itemBookingRepository.deleteById(id);
	}

	@Override
	public Page<ItemBooking> findAll(int page, int count) {
		return this.itemBookingRepository.findAll(PageRequest.of(page, count));
	}

}
