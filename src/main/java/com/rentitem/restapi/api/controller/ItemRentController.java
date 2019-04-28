package com.rentitem.restapi.api.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentitem.restapi.api.entity.Customer;
import com.rentitem.restapi.api.entity.Item;
import com.rentitem.restapi.api.entity.ItemRent;
import com.rentitem.restapi.api.request.ItemRequest;
import com.rentitem.restapi.api.response.Response;
import com.rentitem.restapi.api.service.CustomerService;
import com.rentitem.restapi.api.service.ItemBookingService;
import com.rentitem.restapi.api.service.ItemRentService;
import com.rentitem.restapi.api.service.ItemService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@RestController
@RequestMapping("/api/itemRent")
@CrossOrigin(origins = "*") // Permitindo o acesso de qualquer IP, porta, etc.
@Api(value = "ItemRent")
public class ItemRentController {

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private ItemRentService itemRentService;
	
	@Autowired
	private ItemBookingService itemBookingService;
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping
	@PreAuthorize("hasAnyRole('USER')") 
	@ApiOperation(value = "Aluguel de item", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(@ApiResponse(code = 201, message = "Novo aluguel de item criado", response = ItemRent.class, 
		responseHeaders = @ResponseHeader(name = "Item", description = "Reserva criado", response = ItemRent.class)))
	public ResponseEntity<Response<ItemRent>> create(HttpServletRequest request, 
			@ApiParam(
				    value="ItemRent", 
				    name="itemRent", 
				    required=true)
			@RequestBody ItemRequest itemRequest,
			BindingResult result) {
		Response<ItemRent> response = new Response<ItemRent>();

		try {
			validateCreateItemRent(itemRequest, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			
			Item item = this.itemService.findById(itemRequest.getId());
			Customer customer = this.customerService.findById(itemRequest.getCustomerId());
			ItemRent itemRent = new ItemRent();
			itemRent.setDtRent(LocalDateTime.now());
			itemRent.setDtReturn(LocalDateTime.now().plusWeeks(2L));//Adiciona duas semanas para devolução
			itemRent.setItem(item);
			itemRent.setCustomer(customer);
			ItemRent itemRentPersisted = this.itemRentService.createOrUpdate(itemRent);
			response.setData(itemRentPersisted);
		} catch (DuplicateKeyException dE) {
			response.getErrors().add("Item already registered rent!");
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	private void validateCreateItemRent(ItemRequest itemRequest, BindingResult result) {
		if (itemRequest.getId() == null) {
			result.addError(new ObjectError("ItemRent", "ItemID no information"));
		}else if (this.itemBookingService.findByItemId(itemRequest.getId()) != null) {
			result.addError(new ObjectError("ItemRent", "Item booking already exists"));
		}else if(this.itemRentService.findByItemId(itemRequest.getId()) != null) {
			result.addError(new ObjectError("ItemRent", "Item rent exists"));
		}else if(this.itemService.findById(itemRequest.getId()) == null) {
			result.addError(new ObjectError("ItemRent", "Item not exists"));
		}
		
		if(itemRequest.getCustomerId() == null) {
			result.addError(new ObjectError("ItemRent", "Customer no information"));
		}
	}

	@DeleteMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('USER')")
	@ApiOperation(value = "Remover aluguel do item", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = ItemRent.class),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public ResponseEntity<Response<String>> delete(@PathVariable String id) {
		Response<String> response = new Response<String>();
		ItemRent itemRent = this.itemRentService.findById(id);

		if (itemRent == null) {
			response.getErrors().add("Rent not found! ID: " + id);
			return ResponseEntity.badRequest().body(response);
		}

		this.itemRentService.delete(id);

		return ResponseEntity.ok(new Response<String>());
	}

	@GetMapping(value = "{page}/{count}")
	@PreAuthorize("hasAnyRole('USER')")
	@ApiOperation(value = "Listar todos os itens alugados")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "Page", required = false, dataType = "string", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "count", value = "Count", required = false, dataType = "string", paramType = "query", defaultValue = "10")})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", responseContainer = "Page"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public ResponseEntity<Response<Page<ItemRent>>> findAll(@PathVariable int page, @PathVariable int count) {
		Response<Page<ItemRent>> response = new Response<Page<ItemRent>>();
		Page<ItemRent> items = this.itemRentService.findAll(page, count);
		response.setData(items);

		return ResponseEntity.ok(response);
	}
}
