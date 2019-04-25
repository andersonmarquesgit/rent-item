package com.rentitem.restapi.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRequest {

	private String id;
	private String description;
	private Double price;
	private String itemTypeId;
}
