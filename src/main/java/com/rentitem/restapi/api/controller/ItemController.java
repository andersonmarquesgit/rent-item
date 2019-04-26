package com.rentitem.restapi.api.controller;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentitem.restapi.api.entity.Item;
import com.rentitem.restapi.api.entity.ItemType;
import com.rentitem.restapi.api.request.ItemRequest;
import com.rentitem.restapi.api.response.Response;
import com.rentitem.restapi.api.service.ItemService;
import com.rentitem.restapi.api.service.ItemTypeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@RestController
@RequestMapping("/api/item")
@CrossOrigin(origins = "*") // Permitindo o acesso de qualquer IP, porta, etc.
@Api(value = "Item")
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private ItemTypeService itemTypeService;
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN')") 
	@ApiOperation(value = "Criação de itens", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(@ApiResponse(code = 201, message = "Novo item criado", response = Item.class, 
		responseHeaders = @ResponseHeader(name = "Item", description = "Item criado", response = Item.class)))
	public ResponseEntity<Response<Item>> create(HttpServletRequest request, 
			@ApiParam(
				    value="Item", 
				    name="item", 
				    required=true)
			@RequestBody ItemRequest itemRequest,
			BindingResult result) {
		Response<Item> response = new Response<Item>();

		try {
			Item item = validateCreateItem(itemRequest, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			
			Item itemPersisted = this.itemService.createOrUpdate(item);
			response.setData(itemPersisted);
		} catch (DuplicateKeyException dE) {
			response.getErrors().add("Item already registered!");
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	private Item validateCreateItem(ItemRequest itemRequest, BindingResult result) {
		Item item = new Item();
		ItemType itemType = this.itemTypeService.findById(itemRequest.getItemTypeId());
		
		if (itemRequest.getPrice() == null) {
			result.addError(new ObjectError("Item", "Price no information"));
		}
		
		if (itemRequest.getItemTypeId() == null) {
			result.addError(new ObjectError("Item", "Item type no information"));
		} else if(itemType == null) {
			result.addError(new ObjectError("Item", "Item type no exists"));
		} 
		
		item.setDescription(itemRequest.getDescription());
		item.setPrice(itemRequest.getPrice());
		item.setItemType(itemType);
		
		return item;
	}

	@PutMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value = "Atualização de item", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(@ApiResponse(code = 200, message = "Item atualizado", response = Item.class, 
		responseHeaders = @ResponseHeader(name = "Location", description = "uri do item atualizado", response = String.class)))
	public ResponseEntity<Response<Item>> update(HttpServletRequest request, 
			@ApiParam(
				    value="Item", 
				    name="item", 
				    required=true)
			@RequestBody ItemRequest itemRequest,
			BindingResult result) {
		Response<Item> response = new Response<Item>();
		try {
			Item item = validateUpdateItem(itemRequest, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			
			Item itemPersisted = this.itemService.createOrUpdate(item);
			response.setData(itemPersisted);
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	private Item validateUpdateItem(ItemRequest itemRequest, BindingResult result) {
		Item item = new Item();
		ItemType itemType = this.itemTypeService.findById(itemRequest.getItemTypeId());
		
		if (itemRequest.getId() == null) {
			result.addError(new ObjectError("Item", "Id no information"));
		}
		
		if (itemRequest.getPrice() == null) {
			result.addError(new ObjectError("Item", "Price no information"));
		}
		
		if (itemRequest.getItemTypeId() == null) {
			result.addError(new ObjectError("Item", "Item type no information"));
		} else if(itemType == null) {
			result.addError(new ObjectError("Item", "Item type no exists"));
		} 
		
		item.setId(itemRequest.getId());
		item.setDescription(itemRequest.getDescription());
		item.setPrice(itemRequest.getPrice());
		item.setItemType(itemType);
		
		return item;
	}

	@GetMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value = "Consultar Item pelo ID")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "Id do item", required = false, dataType = "string", paramType = "query", defaultValue = "1") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Item.class),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public ResponseEntity<Response<Item>> findById(@PathVariable String id) {
		Response<Item> response = new Response<Item>();
		Item item = this.itemService.findById(id);

		if (item == null) {
			response.getErrors().add("Register not found! ID: " + id);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(item);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value = "Remover item", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Item.class),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public ResponseEntity<Response<String>> delete(@PathVariable String id) {
		Response<String> response = new Response<String>();
		Item item = this.itemService.findById(id);

		if (item == null) {
			response.getErrors().add("Register not found! ID: " + id);
			return ResponseEntity.badRequest().body(response);
		}

		this.itemService.delete(id);

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
	public ResponseEntity<Response<Page<Item>>> findAll(@PathVariable int page, @PathVariable int count) {
		Response<Page<Item>> response = new Response<Page<Item>>();
		Page<Item> items = this.itemService.findAll(page, count);
		response.setData(items);

		return ResponseEntity.ok(response);
	}
}
