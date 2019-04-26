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

import com.rentitem.restapi.api.entity.Item;
import com.rentitem.restapi.api.entity.ItemBooking;
import com.rentitem.restapi.api.request.ItemRequest;
import com.rentitem.restapi.api.response.Response;
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
@RequestMapping("/api/itemBooking")
@CrossOrigin(origins = "*") // Permitindo o acesso de qualquer IP, porta, etc.
@Api(value = "ItemBooking")
public class ItemBookingController {

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private ItemBookingService itemBookingService;
	
	@Autowired
	private ItemRentService itemRentService;
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN')") 
	@ApiOperation(value = "Reserva de item", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(@ApiResponse(code = 201, message = "Nova reserva de item criado", response = ItemBooking.class, 
		responseHeaders = @ResponseHeader(name = "Item", description = "Reserva criado", response = ItemBooking.class)))
	public ResponseEntity<Response<ItemBooking>> create(HttpServletRequest request, 
			@ApiParam(
				    value="ItemBooking", 
				    name="itemBooking", 
				    required=true)
			@RequestBody ItemRequest itemRequest,
			BindingResult result) {
		Response<ItemBooking> response = new Response<ItemBooking>();

		try {
			validateCreateItemBooking(itemRequest, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			
			Item item = this.itemService.findById(itemRequest.getId());
			ItemBooking itemBooking = new ItemBooking();
			itemBooking.setDtBooking(LocalDateTime.now());
			itemBooking.setDtSchedule(LocalDateTime.now().plusWeeks(2L));//Adiciona duas semanas para o item reservado
			itemBooking.setItem(item);
			ItemBooking itemBookingPersisted = this.itemBookingService.createOrUpdate(itemBooking);
			response.setData(itemBookingPersisted);
		} catch (DuplicateKeyException dE) {
			response.getErrors().add("Item already registered booking!");
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	private void validateCreateItemBooking(ItemRequest itemRequest, BindingResult result) {
		if (itemRequest.getId() == null) {
			result.addError(new ObjectError("ItemBooking", "ItemID no information"));
		}else if (this.itemBookingService.findByItemId(itemRequest.getId()) != null) {
			result.addError(new ObjectError("ItemBooking", "Item booking already exists"));
		}else if(this.itemRentService.findByItemId(itemRequest.getId()) != null) {
			result.addError(new ObjectError("ItemBooking", "Item rent exists"));
		}
	}

	@DeleteMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value = "Remover reserva do item", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = ItemBooking.class),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public ResponseEntity<Response<String>> delete(@PathVariable String id) {
		Response<String> response = new Response<String>();
		ItemBooking itemBooking = this.itemBookingService.findById(id);

		if (itemBooking == null) {
			response.getErrors().add("Booking not found! ID: " + id);
			return ResponseEntity.badRequest().body(response);
		}

		this.itemBookingService.delete(id);

		return ResponseEntity.ok(new Response<String>());
	}

	@GetMapping(value = "{page}/{count}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value = "Listar todos os itens")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "Page", required = false, dataType = "string", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "count", value = "Count", required = false, dataType = "string", paramType = "query", defaultValue = "10")})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", responseContainer = "Page"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public ResponseEntity<Response<Page<ItemBooking>>> findAll(@PathVariable int page, @PathVariable int count) {
		Response<Page<ItemBooking>> response = new Response<Page<ItemBooking>>();
		Page<ItemBooking> items = this.itemBookingService.findAll(page, count);
		response.setData(items);

		return ResponseEntity.ok(response);
	}
}
