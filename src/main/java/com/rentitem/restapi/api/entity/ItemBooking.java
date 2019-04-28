package com.rentitem.restapi.api.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_item_booking")
@Getter
@Setter
public class ItemBooking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	@ManyToOne
	@JoinColumn(name="item_id")
	private Item item;
	
	@Column(name = "dt_booking", columnDefinition = "TIMESTAMP")
    private LocalDateTime dtBooking;
	
	@Column(name = "dt_schedule", columnDefinition = "TIMESTAMP")
    private LocalDateTime dtSchedule;
	
	@ManyToOne
    @JoinColumn(name="customer_id")
	private Customer customer;
}
