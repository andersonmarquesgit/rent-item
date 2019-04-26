package com.rentitem.restapi.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.rentitem.restapi.api.entity.ItemRent;
import com.rentitem.restapi.api.repository.ItemRentRepository;
import com.rentitem.restapi.api.service.ItemRentService;

@Service
public class ItemRentServiceImpl implements ItemRentService {

	@Autowired
	private ItemRentRepository itemRentRepository;
	
	@Override
	public ItemRent createOrUpdate(ItemRent itemRent) {
		return this.itemRentRepository.save(itemRent);
	}

	@Override
	public ItemRent findById(String id) {
		return this.itemRentRepository.findOneById(id);
	}

	@Override
	public void delete(String id) {
		this.itemRentRepository.deleteById(id);
	}

	@Override
	public Page<ItemRent> findAll(int page, int count) {
		return this.itemRentRepository.findAll(PageRequest.of(page, count));
	}

	@Override
	public ItemRent findByItemId(String id) {
		return this.itemRentRepository.findByItemId(id);
	}

}
