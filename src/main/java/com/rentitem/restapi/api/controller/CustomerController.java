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

import com.rentitem.restapi.api.entity.Customer;
import com.rentitem.restapi.api.response.Response;
import com.rentitem.restapi.api.service.CustomerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "*") // Permitindo o acesso de qualquer IP, porta, etc.
@Api(value = "Customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@PostMapping
	@PreAuthorize("hasAnyRole('USER')") 
	@ApiOperation(value = "Criação de clientes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(@ApiResponse(code = 201, message = "Novo cliente criado", response = Customer.class, 
		responseHeaders = @ResponseHeader(name = "Cliente", description = "Cliente criado", response = Customer.class)))
	public ResponseEntity<Response<Customer>> create(HttpServletRequest request, 
			@ApiParam(
				    value="Customer", 
				    name="customer", 
				    required=true)
			@RequestBody Customer customer,
			BindingResult result) {
		Response<Customer> response = new Response<Customer>();

		try {
			validateCreateCustomer(customer, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			Customer customerPersisted = this.customerService.createOrUpdate(customer);
			response.setData(customerPersisted);
		} catch (DuplicateKeyException dE) {
			response.getErrors().add("Email already registered!");
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	private void validateCreateCustomer(Customer customer, BindingResult result) {
		if (customer.getName() == null) {
			result.addError(new ObjectError("Customer", "Name no information"));
		}
		
		if (customer.getEmail() == null) {
			result.addError(new ObjectError("Customer", "Email no information"));
		}
		
		if (customer.getPhone() == null) {
			result.addError(new ObjectError("Customer", "Phone no information"));
		}
	}

	@PutMapping
	@PreAuthorize("hasAnyRole('USER')")
	@ApiOperation(value = "Atualização de clientes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(@ApiResponse(code = 200, message = "Cliente atualizado", response = Customer.class, 
		responseHeaders = @ResponseHeader(name = "Location", description = "uri do cliente atualizado", response = String.class)))
	public ResponseEntity<Response<Customer>> update(HttpServletRequest request, 
			@ApiParam(
				    value="Customer", 
				    name="customer", 
				    required=true)
			@RequestBody Customer customer,
			BindingResult result) {
		Response<Customer> response = new Response<Customer>();
		try {
			validateUpdateCustomer(customer, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			Customer customerPersisted = this.customerService.createOrUpdate(customer);
			response.setData(customerPersisted);
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	private void validateUpdateCustomer(Customer customer, BindingResult result) {
		if (customer.getId() == null) {
			result.addError(new ObjectError("Customer", "Id no information"));
		}
		if (customer.getName() == null) {
			result.addError(new ObjectError("Customer", "Name no information"));
		}
		if (customer.getEmail() == null) {
			result.addError(new ObjectError("Customer", "Email no information"));
		}
		if (customer.getPhone() == null) {
			result.addError(new ObjectError("Customer", "Phone no information"));
		}
	}

	@GetMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('USER')")
	@ApiOperation(value = "Consultar cliente pelo ID")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "Id do cliente", required = false, dataType = "string", paramType = "query", defaultValue = "1") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Customer.class),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public ResponseEntity<Response<Customer>> findById(@PathVariable String id) {
		Response<Customer> response = new Response<Customer>();
		Customer customer = this.customerService.findById(id);

		if (customer == null) {
			response.getErrors().add("Register not found! ID: " + id);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(customer);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('USER')")
	@ApiOperation(value = "Remover cliente", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Customer.class),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public ResponseEntity<Response<String>> delete(@PathVariable String id) {
		Response<String> response = new Response<String>();
		Customer customer = this.customerService.findById(id);

		if (customer == null) {
			response.getErrors().add("Register not found! ID: " + id);
			return ResponseEntity.badRequest().body(response);
		}

		this.customerService.delete(id);

		return ResponseEntity.ok(new Response<String>());
	}

	@GetMapping(value = "{page}/{count}")
	@PreAuthorize("hasAnyRole('USER')")
	@ApiOperation(value = "Listar todos os clientes")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "Page", required = false, dataType = "string", paramType = "query", defaultValue = "0"),
			@ApiImplicitParam(name = "count", value = "Count", required = false, dataType = "string", paramType = "query", defaultValue = "10")})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", responseContainer = "Page"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public ResponseEntity<Response<Page<Customer>>> findAll(@PathVariable int page, @PathVariable int count) {
		Response<Page<Customer>> response = new Response<Page<Customer>>();
		Page<Customer> customers = this.customerService.findAll(page, count);
		response.setData(customers);

		return ResponseEntity.ok(response);
	}
}
