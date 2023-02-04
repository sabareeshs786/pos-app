package com.increff.posapp.pojo;

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
@Table(name = "products")
@Getter
@Setter
@ToString
public class ProductPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(unique = true, nullable = false)
	private String barcode;
	@Column(name = "brand_category", nullable = false)
	private Integer brandCategory;
	@Column(name = "product_name", nullable = false)
	private String name;
	@Column(nullable = false)
	private Double mrp;
}
