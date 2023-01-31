package com.increff.posapp.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inventory")
@Getter
@Setter
public class InventoryPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "quantity", nullable = false)
	private Integer quantity;
	@Column(name = "product_id", nullable = false, unique = true)
	private Integer productId;
	
	@Override
	public String toString() {
		return "InventoryPojo [id=" + id + ", quantity=" + quantity + ", productId=" + productId + "]";
	}
	
}
