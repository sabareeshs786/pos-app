package com.increff.posapp.pojo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
public class OrderPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable = false)
	private ZonedDateTime time;

	public OrderPojo(){}
	public OrderPojo(String zone){
		LocalDateTime localDateTime = LocalDateTime.now();
		ZoneId india = ZoneId.of(zone);
		this.time = ZonedDateTime.of(localDateTime, india);
	}
}
