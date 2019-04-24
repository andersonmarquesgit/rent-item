package com.rentitem.restapi.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_item")
@Getter
@Setter
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	@Column(name = "description")
	private String description;
	
	@NotBlank(message = "Price required")
	@Column(name = "price")
	private Double price;
	
	@ManyToOne
	@JoinColumn(name="item_type_id")
	private ItemType itemType;
}
