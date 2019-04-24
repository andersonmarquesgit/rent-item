package com.rentitem.restapi.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.rentitem.restapi.api.entity.ItemType;
import com.rentitem.restapi.api.repository.ItemTypeRepository;
import com.rentitem.restapi.api.service.ItemTypeService;

@Service
public class ItemTypeServiceImpl implements ItemTypeService {

	@Autowired
	private ItemTypeRepository itemTypeRepository;
	
	@Override
	public ItemType createOrUpdate(ItemType itemType) {
		return this.itemTypeRepository.save(itemType);
	}

	@Override
	public ItemType findById(String id) {
		return this.itemTypeRepository.findOneById(id);
	}

	@Override
	public void delete(String id) {
		this.itemTypeRepository.deleteById(id);
	}

	@Override
	public Page<ItemType> findAll(int page, int count) {
		return this.itemTypeRepository.findAll(PageRequest.of(page, count));
	}

}
