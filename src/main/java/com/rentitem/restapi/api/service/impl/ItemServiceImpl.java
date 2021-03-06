package com.rentitem.restapi.api.service.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.rentitem.restapi.api.entity.Item;
import com.rentitem.restapi.api.repository.ItemRepository;
import com.rentitem.restapi.api.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemRepository itemRepository;
	
	@Override
	public Item createOrUpdate(Item item) {
		return this.itemRepository.save(item);
	}

	@Override
	public Item findById(String id) {
		return this.itemRepository.findOneById(id);
	}

	@Override
	public void delete(String id) {
		this.itemRepository.deleteById(id);
	}

	@Override
	public Page<Item> findAll(int page, int count) {
		return this.itemRepository.findAll(PageRequest.of(page, count));
	}

	@Override
	public List<Item> findItemRentDevolutionPerPeriod() {
		LocalDateTime startDate = LocalDateTime.now().with(LocalTime.MIN);
		LocalDateTime finishDate = startDate.plusWeeks(1L).with(LocalTime.MAX);
		return this.itemRepository.findItemRentDevolutionPerPeriod(startDate, finishDate);
	}

	@Override
	public List<Item> findItemRentPerPeriod() {
		LocalDateTime startDate = LocalDateTime.now().with(LocalTime.MIN);
		LocalDateTime finishDate = startDate.plusWeeks(1L).with(LocalTime.MAX);
		return this.itemRepository.findItemRentPerPeriod(startDate, finishDate);
	}
}
