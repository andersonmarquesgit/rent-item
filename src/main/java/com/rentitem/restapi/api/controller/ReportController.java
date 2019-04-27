package com.rentitem.restapi.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentitem.restapi.api.entity.Item;
import com.rentitem.restapi.api.response.Response;
import com.rentitem.restapi.api.service.ItemService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/report")
@CrossOrigin(origins = "*")//Permitindo o acesso de qualquer IP, porta, etc.
@Api(value = "Report")
public class ReportController {

	@Autowired
	private ItemService itemService;
	
	@GetMapping(value = "/itemDevolution")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value = "Relatório de Itens a serem devolvidos no período semanal, com seus valores", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(@ApiResponse(code = 200, message = "ok", response = Item.class))
	public ResponseEntity<Response<List<Item>>> reportItemRentDevolution(){
		Response<List<Item>> response = new Response<List<Item>>();
		List<Item> itemList = itemService.findItemRentDevolutionPerPeriod();
		response.setData(itemList);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/itemRent")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value = "Relatório de Itens alugados no período semanal, com seus valores", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(@ApiResponse(code = 200, message = "ok", response = Item.class))
	public ResponseEntity<Response<List<Item>>> reportItemRent(){
		Response<List<Item>> response = new Response<List<Item>>();
		List<Item> itemList = itemService.findItemRentPerPeriod();
		response.setData(itemList);
		return ResponseEntity.ok(response);
	}
}
