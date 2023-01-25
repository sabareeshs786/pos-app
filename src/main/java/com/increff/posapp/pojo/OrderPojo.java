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

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "time", nullable = false)
	private ZonedDateTime time;

	public OrderPojo(){

	}
	public OrderPojo(String zone){
		LocalDateTime localDateTime = LocalDateTime.now();
		ZoneId india = ZoneId.of(zone);
		this.time = ZonedDateTime.of(localDateTime, india);
	}
	@Override
	public String toString() {
		return "OrderPojo [id=" + id + ", time=" + time + "]";
	}
}
